package com.creative.greencheck.domain.model

data class Ingredient(
    val id: String? = null,
    val text: String? = null,
    val percentEstimate: Double? = null,
    val vegan: String? = null,
    val vegetarian: String? = null,
    val fromPalmOil: String? = null,
    val subIngredients: List<Ingredient>? = null
)
