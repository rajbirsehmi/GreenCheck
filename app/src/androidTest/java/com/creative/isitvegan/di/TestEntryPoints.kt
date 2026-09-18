package com.creative.isitvegan.di

import com.creative.isitvegan.data.local.AppDatabase
import com.creative.isitvegan.data.remote.OpenFoodFactsApi
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface TestEntryPoint {
    fun openFoodFactsApi(): OpenFoodFactsApi
    fun appDatabase(): AppDatabase
}
