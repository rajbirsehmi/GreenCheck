package com.creative.greencheck.domain.model

import java.util.Locale

data class Ingredient(
    val id: String? = null,
    val text: String? = null,
    val percent: Double? = null,
    val percentEstimate: Double? = null,
    val quantityEstimate: Double? = null,
    val isInTaxonomy: Int? = null,
    val vegan: String? = null,
    val vegetarian: String? = null,
    val fromPalmOil: String? = null,
    val processing: String? = null,
    val subIngredients: List<Ingredient>? = null
) {
    val formattedPercentage: String?
        get() {
            val p = percent ?: percentEstimate
            return p?.let { String.format(Locale.US, "%.1f%%", it) }
        }

    val isVegan: Boolean
        get() = vegan == "yes"

    val isNonVegan: Boolean
        get() = vegan == "no"
}

