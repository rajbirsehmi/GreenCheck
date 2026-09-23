package com.creative.greencheck.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ingredients")
data class IngredientEntity(
    @PrimaryKey val name: String,
    val isVegan: Boolean,
    val description: String = ""
)
