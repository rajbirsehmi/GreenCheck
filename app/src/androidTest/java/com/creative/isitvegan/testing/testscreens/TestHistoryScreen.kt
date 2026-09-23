package com.creative.isitvegan.testing.testscreens

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.creative.isitvegan.MainActivity
import com.creative.isitvegan.data.local.dao.ProductDao
import com.creative.isitvegan.data.local.entity.ProductEntity
import com.creative.isitvegan.testing.robots.RobotEmptyHistoryScreen
import com.creative.isitvegan.testing.robots.RobotHomeScreen
import com.creative.isitvegan.testing.robots.RobotHistoryScreen
import com.creative.isitvegan.testing.robots.RobotWelcomeScreen
import com.sehmi.engine.UiTestEngine
import com.sehmi.engine.createHiltRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TestHistoryScreen {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = UiTestEngine.createHiltRule(MainActivity::class.java)

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
    fun testHistoryScreen_VerifyInitialState() {
        seedProduct()
        UiTestEngine.withRobot(RobotWelcomeScreen()) {
            clickGetStartedIfVisible()
        }
        UiTestEngine.withRobot(RobotHomeScreen()) {
            clickHistory()
        }
        UiTestEngine.withRobot(RobotHistoryScreen()) {
            verifyHistoryScreen()
            verifyClearButton()
        }
    }

    @Test
    fun testHistoryScreen_VerifyEmptyState() {
        UiTestEngine.withRobot(RobotWelcomeScreen()) {
            clickGetStartedIfVisible()
        }
        UiTestEngine.withRobot(RobotHomeScreen()) {
            clickHistory()
        }
        UiTestEngine.withRobot(RobotEmptyHistoryScreen()) {
            verifyEmptyHistoryScreen()
        }
    }

    private fun seedProduct() {
        val barcode = "11112222"
        val seededProduct = ProductEntity(
            barcode = barcode,
            name = "History Tofu",
            brands = "Organic Farm",
            ingredientsAnalysisTags = listOf("en:vegan")
        )

        runBlocking {
            productDao.insertProduct(seededProduct)
        }
    }
}
