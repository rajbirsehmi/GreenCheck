package com.creative.greencheck.testing.testscreens

import android.Manifest
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.creative.greencheck.MainActivity
import com.creative.greencheck.testing.robots.RobotHomeScreen
import com.creative.greencheck.testing.robots.RobotScannerScreen
import com.creative.greencheck.testing.robots.RobotWelcomeScreen
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
class TestScannerScreen {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = UiTestEngine.createHiltRule(MainActivity::class.java)

    @Before
    fun inject() {
        hiltRule.inject()
        UiTestEngine.enablePermission(Manifest.permission.CAMERA)
    }

    @Test
    fun testScannerScreen_VerifyInitialState() {
        UiTestEngine.withRobot(RobotWelcomeScreen()) {
            clickGetStartedIfVisible()
        }
        UiTestEngine.withRobot(RobotHomeScreen()) {
            clickScanner()
        }
        UiTestEngine.withRobot(RobotScannerScreen()) {
            verifyScannerScreen()
            verifyOverlays()
        }
    }
}
