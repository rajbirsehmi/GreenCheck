package com.creative.greencheck.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Ingredients(
    // Basic Info
    @SerialName("id") val id: String? = null,
    @SerialName("text") val text: String? = null,
    @SerialName("percent_estimate") val percentEstimate: Double? = null,
    @SerialName("is_in_taxonomy") val isInTaxonomy: Int? = null,

    // Dietary Status
    @SerialName("vegan") val vegan: String? = null,
    @SerialName("vegetarian") val vegetarian: String? = null,
    @SerialName("from_palm_oil") val fromPalmOil: String? = null,

    // Additional Details
    @SerialName("processing") val processing: String? = null,
    @SerialName("labels") val labels: String? = null,

    // Nested Ingredients
    @SerialName("ingredients") val ingredients: List<Ingredients>? = null
)
