package com.creative.isitvegan.tests

import androidx.compose.ui.text.toUpperCase
import com.creative.isitvegan.MainActivity
import com.creative.isitvegan.data.local.entity.ProductEntity
import com.creative.isitvegan.di.TestEntryPoint
import com.creative.isitvegan.domain.model.Product
import com.creative.isitvegan.testing.robots.HomeRobot
import com.creative.isitvegan.testing.robots.ProductRobot
import com.sehmi.engine.UiTestEngine
import com.sehmi.engine.createHiltRule
import com.sehmi.engine.getTestEntryPoint
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.Locale
import java.util.Locale.getDefault

@HiltAndroidTest
class RecentHistoryTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = UiTestEngine.createHiltRule(MainActivity::class.java)

    private val entryPoint: TestEntryPoint by UiTestEngine.getTestEntryPoint()
    private val database get() = entryPoint.appDatabase()

    private val testProduct = Product(
        barcode = "987654321",
        name = "Vegan Burger",
        brands = "PlantPower",
        ingredientsAnalysisTags = listOf("en:vegan")
    )

    @Before
    fun setup() {
        hiltRule.inject()
        
        runBlocking {
            database.clearAllTables()
            database.productDao().insertProduct(testProduct.toEntity())
        }
    }

    @Test
    fun testRecentHistory_DisplaysScannedProduct() {
        UiTestEngine.withRobot(HomeRobot()) {
            verifyRecentListDisplayed()
            verifyProductInRecentList(testProduct.barcode)
            tapProductInRecentList(testProduct.barcode)
        }

        UiTestEngine.withRobot(ProductRobot()) {
            verifyProductDetails(testProduct.name!!, testProduct.brands!!)
            verifyVeganStatus("CERTIFIED VEGAN FRIENDLY")
        }
    }
}

// Helper extension for mapping to entity if needed in test
private fun Product.toEntity() = ProductEntity(
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
    thumbUrl = thumbUrl,
    timestamp = timestamp
)
