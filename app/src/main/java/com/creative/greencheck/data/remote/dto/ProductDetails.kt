package com.creative.greencheck.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDetails(
    // Identification & General Info
    val id: Long? = null,
    @SerialName("_id") val barcode: String? = null,
    @SerialName("product_name") val name: String? = null,
    @SerialName("brands") val brands: String? = null,
    @SerialName("quantity") val quantity: String? = null,
    @SerialName("product_type") val productType: String? = null,

    // Classification & Metadata
    @SerialName("_keywords") val keywords: List<String>? = null,
    @SerialName("categories") val categories: String? = null,
    @SerialName("data_sources") val dataSources: String? = null,
    @SerialName("ingredients_analysis_tags") val ingredientsAnalysisTags: List<String>? = null,
    @SerialName("labels_tags") val labelsTags: List<String>? = null,

    // Environmental Scores
    @SerialName("ecoscore_grade") val ecoScoreGrade: String? = null,
    @SerialName("ecoscore_score") val ecoScore: Int? = null,

    // Image URLs
    @SerialName("image_front_small_url") val frontSmallUrl: String? = null,
    @SerialName("image_front_thumb_url") val frontThumbUrl: String? = null,
    @SerialName("image_front_url") val frontUrl: String? = null,
    @SerialName("image_ingredients_small_url") val ingredientsSmallUrl: String? = null,
    @SerialName("image_ingredients_thumb_url") val ingredientsThumbUrl: String? = null,
    @SerialName("image_ingredients_url") val ingredientsUrl: String? = null,
    @SerialName("image_nutrition_small_url") val nutritionSmallUrl: String? = null,
    @SerialName("image_nutrition_thumb_url") val nutritionThumbUrl: String? = null,
    @SerialName("image_nutrition_url") val nutritionUrl: String? = null,
    @SerialName("image_small_url") val smallUrl: String? = null,
    @SerialName("image_thumb_url") val thumbUrl: String? = null,
    @SerialName("image_url") val url: String? = null,

    // Ingredients
    @SerialName("ingredients") val ingredients: List<Ingredients>? = null
)
