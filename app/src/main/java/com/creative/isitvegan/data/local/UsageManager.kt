package com.creative.isitvegan.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "usage_prefs")

enum class FeatureType(val limit: Int) {
    SCANNER(10),
    MANUAL_ENTRY(5),
    SEARCH_PRODUCT(10),
    SEARCH_INGREDIENT(15)
}

@Singleton
class UsageManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val LAST_RESET_TIMESTAMP = longPreferencesKey("last_reset_timestamp")
    private val HAS_SEEN_INTRO = booleanPreferencesKey("has_seen_intro")
    private val INSTALLATION_ID = stringPreferencesKey("installation_id")
    
    private fun getFeatureKey(feature: FeatureType) = intPreferencesKey("usage_${feature.name.lowercase()}")

    val hasSeenIntro: Flow<Boolean> = context.dataStore.data.map { it[HAS_SEEN_INTRO] ?: false }

    suspend fun getInstallationId(): String {
        val currentId = context.dataStore.data.map { it[INSTALLATION_ID] }.first()
        if (currentId != null) return currentId

        val newId = UUID.randomUUID().toString()
        context.dataStore.edit { it[INSTALLATION_ID] = newId }
        return newId
    }

    suspend fun setHasSeenIntro(seen: Boolean) {
        context.dataStore.edit { it[HAS_SEEN_INTRO] = seen }
    }

    suspend fun canUseFeature(feature: FeatureType): Boolean {
        checkAndResetDailyLimits()
        val currentUsage = getUsageCount(feature).first()
        return currentUsage < feature.limit
    }

    suspend fun incrementUsage(feature: FeatureType) {
        checkAndResetDailyLimits()
        context.dataStore.edit { prefs ->
            val key = getFeatureKey(feature)
            val current = prefs[key] ?: 0
            prefs[key] = current + 1
        }
    }

    fun getRemainingUsage(feature: FeatureType): Flow<Int> {
        return context.dataStore.data.map { prefs ->
            val key = getFeatureKey(feature)
            val current = prefs[key] ?: 0
            (feature.limit - current).coerceAtLeast(0)
        }
    }

    fun getNextResetTimestamp(): Flow<Long> {
        return context.dataStore.data.map { prefs ->
            val lastReset = prefs[LAST_RESET_TIMESTAMP] ?: 0L
            if (lastReset == 0L) {
                // If never reset, estimate based on now (limits start on first use)
                System.currentTimeMillis() + (24 * 60 * 60 * 1000)
            } else {
                lastReset + (24 * 60 * 60 * 1000)
            }
        }
    }

    private fun getUsageCount(feature: FeatureType): Flow<Int> {
        return context.dataStore.data.map { prefs ->
            prefs[getFeatureKey(feature)] ?: 0
        }
    }

    private suspend fun checkAndResetDailyLimits() {
        val now = System.currentTimeMillis()
        val lastReset = context.dataStore.data.map { it[LAST_RESET_TIMESTAMP] ?: 0L }.first()

        if (now - lastReset > 24 * 60 * 60 * 1000) {
            context.dataStore.edit { prefs ->
                prefs[LAST_RESET_TIMESTAMP] = now
                FeatureType.values().forEach { feature ->
                    prefs[getFeatureKey(feature)] = 0
                }
            }
        }
    }
}
