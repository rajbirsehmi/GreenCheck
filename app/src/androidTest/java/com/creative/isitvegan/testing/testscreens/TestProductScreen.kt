package com.creative.isitvegan.testing.testscreens

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.creative.isitvegan.MainActivity
import com.creative.isitvegan.data.local.dao.ProductDao
import com.creative.isitvegan.data.local.entity.ProductEntity
import com.creative.isitvegan.data.remote.OpenFoodFactsApi
import com.creative.isitvegan.data.remote.dto.ProductDetails
import com.creative.isitvegan.data.remote.dto.ProductResponse
import com.creative.isitvegan.testing.TestTags
import com.creative.isitvegan.testing.robots.RobotHomeScreen
import com.creative.isitvegan.testing.robots.RobotManualEntryScreen
import com.creative.isitvegan.testing.robots.RobotProductScreen
import com.creative.isitvegan.testing.robots.RobotWelcomeScreen
import com.sehmi.engine.UiTestEngine
import com.sehmi.engine.assertions.assertTextContains
import com.sehmi.engine.createHiltRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TestProductScreen {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = UiTestEngine.createHiltRule(MainActivity::class.java)

    @Inject
    lateinit var api: OpenFoodFactsApi

    @Inject
    lateinit var productDao: ProductDao

    @Before
    fun inject() {
        hiltRule.inject()
        runBlocking {
            productDao.deleteAllProducts()
        }
    }

    @Test
    fun testProductScreen_DisplayVeganProduct() {
        val barcode = "12345678"
        val mockProduct = ProductDetails(
            barcode = barcode,
            name = "Vegan Milk",
            brands = "EcoBrand",
            ingredientsAnalysisTags = listOf("en:vegan")
        )
        
        coEvery { api.getProduct(barcode, any()) } returns ProductResponse(
            status = 1,
            product = mockProduct
        )

        UiTestEngine.withRobot(RobotWelcomeScreen()) {
            clickGetStartedIfVisible()
        }
        UiTestEngine.withRobot(RobotHomeScreen()) {
            clickManual()
        }
        UiTestEngine.withRobot(RobotManualEntryScreen()) {
            enterBarcode(barcode)
            clickIdentify()
        }
        UiTestEngine.withRobot(RobotProductScreen()) {
            waitForProductLoaded()
            verifyProductHero()
            verifyStatusBanner()
            verifyProductDetails()
        }
    }

    @Test
    fun testProductScreen_DisplaySeededProduct() {
        val barcode = "99999999"
        val seededProduct = ProductEntity(
            barcode = barcode,
            name = "Seeded Tofu",
            brands = "Organic Farm",
            ingredientsAnalysisTags = listOf("en:vegan")
        )
        
        runBlocking {
            productDao.insertProduct(seededProduct)
        }

        UiTestEngine.withRobot(RobotWelcomeScreen()) {
            clickGetStartedIfVisible()
        }
        UiTestEngine.withRobot(RobotHomeScreen()) {
            clickManual()
        }
        UiTestEngine.withRobot(RobotManualEntryScreen()) {
            enterBarcode(barcode)
            clickIdentify()
        }
        
        UiTestEngine.withRobot(RobotProductScreen()) {
            waitForProductLoaded()
            verifyProductHero()
            assertTextContains(TestTags.V2.Product.NAME, "Seeded Tofu")
            assertTextContains(TestTags.V2.Product.BRAND_NAME, "ORGANIC FARM")
        }
    }
}
