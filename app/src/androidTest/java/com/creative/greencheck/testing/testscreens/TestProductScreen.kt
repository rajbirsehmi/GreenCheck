package com.creative.greencheck.testing.testscreens

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.creative.greencheck.MainActivity
import com.creative.greencheck.data.local.UsageManager
import com.creative.greencheck.data.local.dao.ProductDao
import com.creative.greencheck.data.local.entity.ProductEntity
import com.creative.greencheck.data.remote.OpenFoodFactsApi
import com.creative.greencheck.data.remote.dto.ProductDetails
import com.creative.greencheck.data.remote.dto.ProductResponse
import com.creative.greencheck.testing.TestTags
import com.creative.greencheck.testing.robots.RobotHomeScreen
import com.creative.greencheck.testing.robots.RobotManualEntryScreen
import com.creative.greencheck.testing.robots.RobotProductScreen
import com.creative.greencheck.testing.robots.RobotWelcomeScreen
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

    @Inject
    lateinit var usageManager: UsageManager

    @Before
    fun inject() {
        hiltRule.inject()
        runBlocking {
            usageManager.setHasSeenIntro(true)
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
        
        coEvery { api.getProduct(barcode, null) } returns ProductResponse(
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
