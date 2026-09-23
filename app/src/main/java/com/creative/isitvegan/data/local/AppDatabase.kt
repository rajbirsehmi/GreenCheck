package com.creative.isitvegan.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.creative.isitvegan.data.local.converters.RoomConverters
import com.creative.isitvegan.data.local.dao.ProductDao
import com.creative.isitvegan.data.local.entity.IngredientEntity
import com.creative.isitvegan.data.local.entity.ProductEntity

@Database(entities = [ProductEntity::class, IngredientEntity::class], version = 4)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}