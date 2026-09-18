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
    val name: String? = null,
    val brands: String? = null,
    val quantity: String? = null,
    val productType: String? = null,
    val keywords: List<String>? = null,
    val categories: String? = null,
    val dataSources: String? = null,
    val ingredientsAnalysisTags: List<String>? = null,
    val labelsTags: List<String>? = null,
    val ecoScoreGrade: String? = null,
    val ecoScore: Int? = null,
    val frontSmallUrl: String? = null,
    val frontThumbUrl: String? = null,
    val frontUrl: String? = null,
    val ingredientsSmallUrl: String? = null,
    val ingredientsThumbUrl: String? = null,
    val ingredientsUrl: String? = null,
    val nutritionSmallUrl: String? = null,
    val nutritionThumbUrl: String? = null,
    val nutritionUrl: String? = null,
    val smallUrl: String? = null,
    val thumbUrl: String? = null,
    val url: String? = null,
    val ingredients: List<Ingredients>? = null,
    val timestamp: Long = System.currentTimeMillis()
)