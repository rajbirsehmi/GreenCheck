package com.creative.greencheck.data.remote

import java.util.Locale

object LocaleDetector {

    /**
     * Returns the OpenFoodFacts compatible country tag for the current device region (e.g., "en:india", "en:united-states").
     */
    val currentDeviceCountryTag: String
        get() {
            val regionCode = Locale.getDefault().country
            return if (regionCode.isNullOrBlank()) {
                "en:united-states" // Default fallback
            } else {
                countryTag(regionCode)
            }
        }

    /**
     * Converts a region code into an OFF-compatible country tag format.
     */
    fun countryTag(regionCode: String): String {
        val normalizedCode = regionCode.lowercase(Locale.ROOT)

        // Map common country codes to OpenFoodFacts tag naming conventions
        return when (normalizedCode) {
            "in" -> "en:india"
            "us" -> "en:united-states"
            "gb", "uk" -> "en:united-kingdom"
            "ca" -> "en:canada"
            "au" -> "en:australia"
            "de" -> "en:germany"
            "fr" -> "en:france"
            "jp" -> "en:japan"
            "mx" -> "en:mexico"
            "br" -> "en:brazil"
            "it" -> "en:italy"
            "es" -> "en:spain"
            "nl" -> "en:netherlands"
            "ch" -> "en:switzerland"
            "se" -> "en:sweden"
            "nz" -> "en:new-zealand"
            else -> "en:$normalizedCode"
        }
    }
}
