package com.creative.isitvegan.testing.testflows

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.creative.isitvegan.MainActivity
import com.creative.isitvegan.data.local.UsageManager
import com.creative.isitvegan.data.remote.OpenFoodFactsApi
import com.creative.isitvegan.data.remote.dto.ProductDetails
import com.creative.isitvegan.data.remote.dto.ProductResponse
import com.creative.isitvegan.data.remote.dto.SearchResponse
import com.creative.isitvegan.testing.robots.RobotHomeScreen
import com.creative.isitvegan.testing.robots.RobotManualEntryScreen
import com.creative.isitvegan.testing.robots.RobotProductScreen
import com.creative.isitvegan.testing.robots.RobotSearchScreen
import com.creative.isitvegan.testing.robots.RobotWelcomeScreen
import com.sehmi.engine.UiTestEngine
import com.sehmi.engine.actions.pressBack
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
class FlowProductIdentification {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = UiTestEngine.createHiltRule(MainActivity::class.java)

    @Inject
    lateinit var api: OpenFoodFactsApi

    @Inject
    lateinit var usageManager: UsageManager

    @Before
    fun inject() {
        hiltRule.inject()
        runBlocking {
            usageManager.setHasSeenIntro(true)
        }
    }

    @Test
    fun testManualEntryToProductDetail() {
        val barcode = "87654321"
        coEvery { api.getProduct(barcode, null) } returns ProductResponse(
            status = 1,
            product = ProductDetails(barcode = barcode, name = "Manual Product", brands = "Brand A")
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
            verifyProductHero()
        }
    }

    @Test
    fun testSearchToProductDetail() {
        val query = "Oat"
        val barcode = "11223344"
        val mockProduct = ProductDetails(barcode = barcode, name = "Oat Milk", brands = "Brand B")
        
        coEvery { api.searchProducts(query, any()) } returns SearchResponse(
            products = listOf(mockProduct)
        )
        coEvery { api.searchByIngredient(query, any()) } returns SearchResponse(
            products = listOf(mockProduct)
        )
        coEvery { api.getProduct(barcode, null) } returns ProductResponse(
            status = 1,
            product = mockProduct
        )
        UiTestEngine.withRobot(RobotWelcomeScreen()) {
            clickGetStartedIfVisible()
        }

        UiTestEngine.withRobot(RobotHomeScreen()) {
            clickSearch()
        }
        UiTestEngine.withRobot(RobotSearchScreen()) {
            enterSearchQuery(query)
            clickSearch()
            clickProductItem(barcode)
        }
        UiTestEngine.withRobot(RobotProductScreen()) {
            verifyProductHero()
        }
    }
}
