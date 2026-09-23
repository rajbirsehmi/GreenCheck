package com.creative.greencheck.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.creative.greencheck.data.local.converters.RoomConverters
import com.creative.greencheck.data.local.dao.ProductDao
import com.creative.greencheck.data.local.entity.IngredientEntity
import com.creative.greencheck.data.local.entity.ProductEntity

@Database(entities = [ProductEntity::class, IngredientEntity::class], version = 4)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}