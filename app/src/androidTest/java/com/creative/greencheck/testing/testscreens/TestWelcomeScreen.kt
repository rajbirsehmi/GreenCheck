package com.creative.greencheck.testing.testscreens

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.creative.greencheck.MainActivity
import com.creative.greencheck.data.local.AppDatabase
import com.creative.greencheck.data.local.UsageManager
import com.creative.greencheck.testing.robots.RobotWelcomeScreen
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
class TestWelcomeScreen {

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val rule = UiTestEngine.createHiltRule(MainActivity::class.java)

    @Inject
    lateinit var usageManager: UsageManager

    @Inject
    lateinit var appDatabase: AppDatabase

    @Before
    fun inject() {
        hiltRule.inject()
        runBlocking {
            usageManager.setHasSeenIntro(false)
            appDatabase.clearAllTables()
        }
    }

    @Test
    fun testWelcomeScreen_checkIf_LogoIsDisplayed() {
        UiTestEngine.withRobot(RobotWelcomeScreen()) {
            verifyThatLogoIsBeingDisplayed()
        }
    }

    @Test
    fun testWelcomeScreen_checkIf_TitleIsDisplayed() {
        UiTestEngine.withRobot(RobotWelcomeScreen()) {
            verifyThatTitleIsBeingDisplayed()
        }
    }

    @Test
    fun testWelcomeScreen_checkIf_SubtitleIsDisplayed() {
        UiTestEngine.withRobot(RobotWelcomeScreen()) {
            verifyThatSubtitleIsBeingDisplayed()
        }
    }

    @Test
    fun  testWelcomeScreen_checkIf_DescriptionTextIsDisplayed() {
        UiTestEngine.withRobot(RobotWelcomeScreen()) {
            verifyThatDescriptionTextIsBeingDisplayed()
        }
    }

    @Test
    fun testWelcomeScreen_checkIf_GetStartedButtonIsDisplayed() {
        UiTestEngine.withRobot(RobotWelcomeScreen()) {
            verifyThatGetStartedButtonIsBeingDisplayed()
        }
    }

    @Test
    fun testWelcomeScreen_checkIf_HowWeHandleYourDataButtonIsDisplayed() {
        UiTestEngine.withRobot(RobotWelcomeScreen()) {
            verifyThatHowWeHandleTheDataButtonIsBeingDisplayed()
        }
    }
}