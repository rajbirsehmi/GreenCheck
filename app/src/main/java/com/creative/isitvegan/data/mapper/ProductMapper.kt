package com.creative.isitvegan.data.mapper

import com.creative.isitvegan.data.local.entity.ProductEntity
import com.creative.isitvegan.data.remote.dto.Ingredients
import com.creative.isitvegan.data.remote.dto.ProductDetails
import com.creative.isitvegan.domain.model.Ingredient
import com.creative.isitvegan.domain.model.Product

fun ProductDetails.toDomain(): Product {
    return Product(
        barcode = barcode ?: "",
        name = name,
        brands = brands,
        quantity = quantity,
        productType = productType,
        keywords = keywords,
        categories = categories,
        dataSources = dataSources,
        ingredientsAnalysisTags = ingredientsAnalysisTags,
        labelsTags = labelsTags,
        ecoScoreGrade = ecoScoreGrade,
        ecoScore = ecoScore,
        imageUrl = url,
        thumbUrl = thumbUrl,
        ingredientsImageUrl = ingredientsUrl,
        nutritionImageUrl = nutritionUrl,
        ingredients = ingredients?.map { it.toDomain() }
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
        dataSources = dataSources,
        ingredientsAnalysisTags = ingredientsAnalysisTags,
        labelsTags = labelsTags,
        ecoScoreGrade = ecoScoreGrade,
        ecoScore = ecoScore,
        frontSmallUrl = null, // Simplified for entity if not needed, or map correctly
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
        timestamp = timestamp
    )
}

fun Ingredients.toDomain(): Ingredient {
    return Ingredient(
        id = id,
        text = text,
        percentEstimate = percentEstimate,
        vegan = vegan,
        vegetarian = vegetarian,
        fromPalmOil = fromPalmOil,
        subIngredients = ingredients?.map { it.toDomain() }
    )
}

fun Ingredient.toDto(): Ingredients {
    return Ingredients(
        id = id,
        text = text,
        percentEstimate = percentEstimate,
        vegan = vegan,
        vegetarian = vegetarian,
        fromPalmOil = fromPalmOil,
        ingredients = subIngredients?.map { it.toDto() }
    )
}
