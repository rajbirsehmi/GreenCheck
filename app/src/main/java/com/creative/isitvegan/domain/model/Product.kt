package com.creative.isitvegan.domain.model

data class Product(
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
    val imageUrl: String?,
    val thumbUrl: String?,
    val ingredientsImageUrl: String?,
    val nutritionImageUrl: String?,
    val ingredients: List<Ingredient>?,
    val timestamp: Long = System.currentTimeMillis()
) {
    val isVegan: Boolean
        get() = ingredientsAnalysisTags?.contains("en:vegan") == true

    val isNonVegan: Boolean
        get() = ingredientsAnalysisTags?.contains("en:non-vegan") == true

    val statusText: String
        get() = when {
            isVegan -> "Certified Vegan Friendly"
            isNonVegan -> "Contains Non-Vegan Ingredients"
            else -> "Unknown Vegan Status"
        }
}
