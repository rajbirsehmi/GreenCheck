package com.creative.greencheck.di

import com.creative.greencheck.data.local.AppDatabase
import com.creative.greencheck.data.remote.OpenFoodFactsApi
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface TestEntryPoint {
    fun openFoodFactsApi(): OpenFoodFactsApi
    fun appDatabase(): AppDatabase
}
