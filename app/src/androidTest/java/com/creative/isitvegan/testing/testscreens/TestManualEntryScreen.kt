package com.creative.isitvegan.testing.testscreens

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.creative.isitvegan.MainActivity
import com.creative.isitvegan.testing.robots.RobotHomeScreen
import com.creative.isitvegan.testing.robots.RobotManualEntryScreen
import com.creative.isitvegan.testing.robots.RobotWelcomeScreen
import com.sehmi.engine.UiTestEngine
import com.sehmi.engine.createHiltRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class TestManualEntryScreen {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = UiTestEngine.createHiltRule(MainActivity::class.java)

    @Before
    fun inject() {
        hiltRule.inject()
    }

    @Test
    fun testManualEntryScreen_VerifyInitialState() {
        UiTestEngine.withRobot(RobotWelcomeScreen()) {
            clickGetStartedIfVisible()
        }
        UiTestEngine.withRobot(RobotHomeScreen()) {
            clickManual()
        }
        UiTestEngine.withRobot(RobotManualEntryScreen()) {
            verifyManualEntryScreen()
            verifyQuota()
        }
    }
}
