package com.creative.isitvegan.domain.model

data class Ingredient(
    val id: String?,
    val text: String?,
    val percentEstimate: Double?,
    val vegan: String?,
    val vegetarian: String?,
    val fromPalmOil: String?,
    val subIngredients: List<Ingredient>? = null
)
