package com.creative.greencheck.domain.model

data class Product(
    val barcode: String,
    val name: String? = null,
    val brands: String? = null,
    val quantity: String? = null,
    val productType: String? = null,
    val keywords: List<String>? = null,
    val categories: String? = null,
    val categoriesTags: List<String>? = null,
    val dataSources: String? = null,
    val ingredientsAnalysisTags: List<String>? = null,
    val labelsTags: List<String>? = null,
    val ecoScoreGrade: String? = null,
    val ecoScore: Int? = null,
    val imageUrl: String? = null,
    val thumbUrl: String? = null,
    val ingredientsImageUrl: String? = null,
    val nutritionImageUrl: String? = null,
    val ingredients: List<Ingredient>? = null,
    val ingredientsText: String? = null,
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

    val formattedCategories: List<String>
        get() = categoriesTags?.mapNotNull { tag ->
            tag.removePrefix("en:")
                .replace("-", " ")
                .split(" ")
                .joinToString(" ") { word -> word.replaceFirstChar { char -> char.uppercase() } }
                .takeIf { it.isNotBlank() }
        } ?: categories?.split(",")?.map { it.trim() }?.filter { it.isNotBlank() } ?: emptyList()

    val formattedLabels: List<String>
        get() = labelsTags?.mapNotNull { tag ->
            tag.removePrefix("en:")
                .replace("-", " ")
                .split(" ")
                .joinToString(" ") { word -> word.replaceFirstChar { char -> char.uppercase() } }
                .takeIf { it.isNotBlank() }
        } ?: emptyList()

    val isPalmOilFree: Boolean
        get() = ingredientsAnalysisTags?.contains("en:palm-oil-free") == true

    val hasPalmOil: Boolean
        get() = ingredientsAnalysisTags?.contains("en:palm-oil") == true

    val palmOilStatusText: String?
        get() = when {
            isPalmOilFree -> "Palm Oil Free"
            hasPalmOil -> "Contains Palm Oil"
            ingredientsAnalysisTags?.contains("en:palm-oil-content-unknown") == true -> "Palm Oil Unknown"
            else -> null
        }

}

