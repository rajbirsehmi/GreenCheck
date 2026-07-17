package com.creative.isitvegan.ui

import com.creative.isitvegan.MainActivity
import com.creative.isitvegan.core.testing.BaseRobotTest
import com.creative.isitvegan.core.testing.robots.homeRobot
import com.creative.isitvegan.core.testing.robots.productRobot
import com.creative.isitvegan.data.local.AppDatabase
import com.creative.isitvegan.data.local.dao.ProductDao
import com.creative.isitvegan.data.local.entity.ProductEntity
import com.creative.isitvegan.data.remote.dto.Ingredients
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class ProductScreenTest : BaseRobotTest<MainActivity>(MainActivity::class.java) {

    @Inject
    lateinit var database: AppDatabase

    @Inject
    lateinit var dao: ProductDao

    @Before
    fun setup() {
        hiltRule.inject()
        // Ensure database operations are finished before activity is fully ready
        runBlocking {
            database.clearAllTables()
            dao.insertProduct(veganProduct)
            dao.insertProduct(nonVeganProduct)
        }
    }

    @Test
    fun productScreen_displaysVeganProductCorrectly() {
        // Act & Assert
        homeRobot(composeTestRule) {
            verifyProductInRecentList("Vegan Delight")
            clickProduct("Vegan Delight")
        }

        productRobot(composeTestRule) {
            verifyProductDetails("Vegan Delight", "Green Earth")
            verifyVeganStatus()
        }
    }

    @Test
    fun productScreen_displaysNonVeganProductCorrectly() {
        homeRobot(composeTestRule) {
            verifyProductInRecentList("Beef Jerky")
            clickProduct("Beef Jerky")
        }

        productRobot(composeTestRule) {
            verifyProductDetails("Beef Jerky", "Meat Co")
            verifyNonVeganStatus()
        }
    }

    private val veganProduct = ProductEntity(
        barcode = "543216789",
        name = "Vegan Delight",
        brands = "Green Earth",
        quantity = "200g",
        productType = "Food",
        keywords = listOf("vegan", "snack"),
        categories = "Snacks",
        dataSources = "OpenFoodFacts",
        ingredientsAnalysisTags = listOf("en:vegan"),
        labelsTags = listOf("en:vegan"),
        ecoScoreGrade = "a",
        ecoScore = 90,
        frontSmallUrl = null,
        frontThumbUrl = null,
        frontUrl = null,
        ingredientsSmallUrl = null,
        ingredientsThumbUrl = null,
        ingredientsUrl = null,
        nutritionSmallUrl = null,
        nutritionThumbUrl = null,
        nutritionUrl = null,
        smallUrl = null,
        thumbUrl = null,
        url = null,
        ingredients = emptyList<Ingredients>(),
        timestamp = 1000L
    )

    private val nonVeganProduct = ProductEntity(
        barcode = "987654321",
        name = "Beef Jerky",
        brands = "Meat Co",
        quantity = "100g",
        productType = "Food",
        keywords = listOf("meat", "snack"),
        categories = "Meat",
        dataSources = "OpenFoodFacts",
        ingredientsAnalysisTags = listOf("en:non-vegan"),
        labelsTags = listOf("en:non-vegan"),
        ecoScoreGrade = "e",
        ecoScore = 20,
        frontSmallUrl = null,
        frontThumbUrl = null,
        frontUrl = null,
        ingredientsSmallUrl = null,
        ingredientsThumbUrl = null,
        ingredientsUrl = null,
        nutritionSmallUrl = null,
        nutritionThumbUrl = null,
        nutritionUrl = null,
        smallUrl = null,
        thumbUrl = null,
        url = null,
        ingredients = emptyList<Ingredients>(),
        timestamp = 2000L
    )
}
