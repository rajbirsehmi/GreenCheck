package com.creative.greencheck.testing.testscreens

//import androidx.test.ext.junit.runners.AndroidJUnit4
//import com.creative.greencheck.MainActivity
//import com.creative.greencheck.data.local.UsageManager
//import com.creative.greencheck.data.remote.OpenFoodFactsApi
//import com.creative.greencheck.data.remote.dto.ProductDetails
//import com.creative.greencheck.data.remote.dto.SearchResponse
//import com.creative.greencheck.testing.robots.RobotHomeScreen
//import com.creative.greencheck.testing.robots.RobotSearchScreen
//import com.creative.greencheck.testing.robots.RobotWelcomeScreen
//import com.sehmi.engine.UiTestEngine
//import com.sehmi.engine.createHiltRule
//import dagger.hilt.android.testing.HiltAndroidRule
//import dagger.hilt.android.testing.HiltAndroidTest
//import io.mockk.coEvery
//import kotlinx.coroutines.runBlocking
//import org.junit.Before
//import org.junit.Rule
//import org.junit.runner.RunWith
//import javax.inject.Inject
//
//@HiltAndroidTest
//@RunWith(AndroidJUnit4::class)
//class TestSearchScreen {
//
//    @get:Rule(order = 0)
//    var hiltRule = HiltAndroidRule(this)
//
//    @get:Rule(order = 1)
//    val rule = UiTestEngine.createHiltRule(MainActivity::class.java)
//
//    @Inject
//    lateinit var usageManager: UsageManager
//
//    @Inject
//    lateinit var api: OpenFoodFactsApi
//
//    @Before
//    fun inject() {
//        hiltRule.inject()
//        runBlocking {
//            usageManager.setHasSeenIntro(true)
//        }
//    }
//
//    //    @Test
//    fun testSearchScreen_VerifyInitialState() {
//        UiTestEngine.withRobot(RobotWelcomeScreen()) {
//            clickGetStartedIfVisible()
//        }
//        UiTestEngine.withRobot(RobotHomeScreen()) {
//            clickSearch()
//        }
//        UiTestEngine.withRobot(RobotSearchScreen()) {
//            waitForResults()
//            verifySearchScreen()
//            verifySearchModes()
//            verifyQuota()
//        }
//    }
//
//    //    @Test
//    fun testSearchScreen_PerformIngredientSearch() {
//        val ingredient = "Soy"
//        val barcode = "55667788"
//        val mockProduct = ProductDetails(
//            barcode = barcode,
//            name = "Soy Milk",
//            brands = "Brand Soy"
//        )
//
//        coEvery {
//            api.searchByIngredient(
//                ingredient,
//                any(),
//                any(),
//                any(),
//                any(),
//                any(),
//                any()
//            )
//        } returns SearchResponse(
//            products = listOf(mockProduct)
//        )
//
//        UiTestEngine.withRobot(RobotWelcomeScreen()) {
//            clickGetStartedIfVisible()
//        }
//        UiTestEngine.withRobot(RobotHomeScreen()) {
//            clickSearch()
//        }
//        UiTestEngine.withRobot(RobotSearchScreen()) {
//            enterSearchQuery(ingredient)
//            clickSearch()
//        }
//    }
//}
