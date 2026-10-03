package com.creative.greencheck.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.creative.greencheck.data.local.converters.RoomConverters
import com.creative.greencheck.data.local.dao.ProductDao
import com.creative.greencheck.data.local.entity.IngredientEntity
import com.creative.greencheck.data.local.entity.ProductEntity

@Database(entities = [ProductEntity::class, IngredientEntity::class], version = 5)
@TypeConverters(RoomConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao

    companion object {
        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE products ADD COLUMN categoriesTags TEXT")
                db.execSQL("ALTER TABLE products ADD COLUMN ingredientsText TEXT")
            }
        }
    }
}
