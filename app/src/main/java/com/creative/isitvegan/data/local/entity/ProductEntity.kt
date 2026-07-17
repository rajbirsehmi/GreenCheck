package com.creative.isitvegan.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.creative.isitvegan.data.remote.dto.Ingredients
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val barcode: String,
    val name: String?,
    val brands: String?,
    val quantity: String?,
    val productType: String?,
    val keywords: List<String>?,
    val categories: String?,
    val dataSources: String?,
    val ingredientsAnalysisTags: List<String>?,
    val labelsTags: List<String>?,
    val ecoScoreGrade: String?,
    val ecoScore: Int?,
    val frontSmallUrl: String?,
    val frontThumbUrl: String?,
    val frontUrl: String?,
    val ingredientsSmallUrl: String?,
    val ingredientsThumbUrl: String?,
    val ingredientsUrl: String?,
    val nutritionSmallUrl: String?,
    val nutritionThumbUrl: String?,
    val nutritionUrl: String?,
    val smallUrl: String?,
    val thumbUrl: String?,
    val url: String?,
    val ingredients: List<Ingredients>?,
    val timestamp: Long = System.currentTimeMillis()
)