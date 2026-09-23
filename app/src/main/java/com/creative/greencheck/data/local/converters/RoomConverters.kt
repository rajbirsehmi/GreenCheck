package com.creative.greencheck.data.local.converters

import androidx.room.TypeConverter
import com.creative.greencheck.data.remote.dto.Ingredients
import kotlinx.serialization.json.Json

class RoomConverters {
    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return value?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        return value?.let { json.decodeFromString(it) }
    }

    @TypeConverter
    fun fromIngredientsList(value: List<Ingredients>?): String? {
        return value?.let { json.encodeToString(it) }
    }

    @TypeConverter
    fun toIngredientsList(value: String?): List<Ingredients>? {
        return value?.let { json.decodeFromString(it) }
    }
}