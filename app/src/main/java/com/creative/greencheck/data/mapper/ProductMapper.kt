package com.creative.greencheck.data.mapper

import com.creative.greencheck.data.local.entity.ProductEntity
import com.creative.greencheck.data.remote.dto.Ingredients
import com.creative.greencheck.data.remote.dto.ProductDetails
import com.creative.greencheck.domain.model.Ingredient
import com.creative.greencheck.domain.model.Product

fun ProductDetails.toDomain(): Product {
    val mappedBarcode = code?.takeIf { it.isNotBlank() } ?: barcode ?: ""
    val mappedCategories = categories?.takeIf { it.isNotBlank() }
        ?: categoriesTags?.joinToString(", ") {
            it.removePrefix("en:").replace("-", " ").replaceFirstChar { char -> char.uppercase() }
        }

    val primaryImageUrl = frontUrl?.takeIf { it.isNotBlank() } ?: url?.takeIf { it.isNotBlank() } ?: smallUrl?.takeIf { it.isNotBlank() }
    val primaryThumbUrl = frontThumbUrl?.takeIf { it.isNotBlank() } ?: thumbUrl?.takeIf { it.isNotBlank() } ?: frontSmallUrl?.takeIf { it.isNotBlank() } ?: primaryImageUrl

    return Product(
        barcode = mappedBarcode,
        name = name,
        brands = brands,
        quantity = quantity,
        productType = productType,
        keywords = keywords,
        categories = mappedCategories,
        categoriesTags = categoriesTags,
        dataSources = dataSources,
        ingredientsAnalysisTags = ingredientsAnalysisTags,
        labelsTags = labelsTags,
        ecoScoreGrade = ecoScoreGrade,
        ecoScore = ecoScore,
        imageUrl = primaryImageUrl,
        thumbUrl = primaryThumbUrl,
        ingredientsImageUrl = ingredientsUrl,
        nutritionImageUrl = nutritionUrl,
        ingredients = ingredients?.map { it.toDomain() },
        ingredientsText = ingredientsText
    )
}

fun ProductEntity.toDomain(): Product {
    return Product(
        barcode = barcode,
        name = name,
        brands = brands,
        quantity = quantity,
        productType = productType,
        keywords = keywords,
        categories = categories,
        categoriesTags = categoriesTags,
        dataSources = dataSources,
        ingredientsAnalysisTags = ingredientsAnalysisTags,
        labelsTags = labelsTags,
        ecoScoreGrade = ecoScoreGrade,
        ecoScore = ecoScore,
        imageUrl = url,
        thumbUrl = thumbUrl,
        ingredientsImageUrl = ingredientsUrl,
        nutritionImageUrl = nutritionUrl,
        ingredients = ingredients?.map { it.toDomain() },
        ingredientsText = ingredientsText,
        timestamp = timestamp
    )
}

fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        barcode = barcode,
        name = name,
        brands = brands,
        quantity = quantity,
        productType = productType,
        keywords = keywords,
        categories = categories,
        categoriesTags = categoriesTags,
        dataSources = dataSources,
        ingredientsAnalysisTags = ingredientsAnalysisTags,
        labelsTags = labelsTags,
        ecoScoreGrade = ecoScoreGrade,
        ecoScore = ecoScore,
        frontSmallUrl = null,
        frontThumbUrl = null,
        frontUrl = null,
        ingredientsSmallUrl = null,
        ingredientsThumbUrl = null,
        ingredientsUrl = ingredientsImageUrl,
        nutritionSmallUrl = null,
        nutritionThumbUrl = null,
        nutritionUrl = nutritionImageUrl,
        smallUrl = null,
        thumbUrl = thumbUrl,
        url = imageUrl,
        ingredients = ingredients?.map { it.toDto() },
        ingredientsText = ingredientsText,
        timestamp = timestamp
    )
}

fun Ingredients.toDomain(): Ingredient {
    return Ingredient(
        id = id,
        text = text,
        percent = percent,
        percentEstimate = percentEstimate,
        quantityEstimate = quantityEstimate,
        isInTaxonomy = isInTaxonomy,
        vegan = vegan,
        vegetarian = vegetarian,
        fromPalmOil = fromPalmOil,
        processing = processing,
        subIngredients = ingredients?.map { it.toDomain() }
    )
}

fun Ingredient.toDto(): Ingredients {
    return Ingredients(
        id = id,
        text = text,
        percent = percent,
        percentEstimate = percentEstimate,
        quantityEstimate = quantityEstimate,
        isInTaxonomy = isInTaxonomy,
        vegan = vegan,
        vegetarian = vegetarian,
        fromPalmOil = fromPalmOil,
        processing = processing,
        ingredients = subIngredients?.map { it.toDto() }
    )
}

