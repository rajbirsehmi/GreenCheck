package com.creative.isitvegan.testing.robots

import com.creative.isitvegan.testing.TestTags
import com.sehmi.engine.actions.clickOnTag
import com.sehmi.engine.assertions.assertTagDisplayed
import com.sehmi.engine.conditions.isDisplayed
import com.sehmi.engine.core.ComposeRuleScope

class RobotWelcomeScreen : ComposeRuleScope {

    fun checkIfBetStartedButtonIsDisplayed() {
        assertTagDisplayed(TestTags.V2.Welcome.BTN_GET_STARTED)
    }

    fun verifyThatLogoIsBeingDisplayed() {
        assertTagDisplayed(TestTags.V2.Welcome.LOGO)
    }

    fun verifyThatTitleIsBeingDisplayed() {
        assertTagDisplayed(TestTags.V2.Welcome.TITLE)
    }

    fun verifyThatSubtitleIsBeingDisplayed() {
        assertTagDisplayed(TestTags.V2.Welcome.SUBTITLE)
    }

    fun verifyThatDescriptionTextIsBeingDisplayed() {
        assertTagDisplayed(TestTags.V2.Welcome.DESCRIPTION)
    }

    fun verifyThatGetStartedButtonIsBeingDisplayed() {
        assertTagDisplayed(TestTags.V2.Welcome.BTN_GET_STARTED)
    }

    fun verifyThatHowWeHandleTheDataButtonIsBeingDisplayed() {
        assertTagDisplayed(TestTags.V2.Welcome.BTN_HOW_WE_HANDLE_DATA)
    }

    fun isWelcomeScreenVisible(): Boolean {
        return isDisplayed(TestTags.V2.Welcome.BTN_GET_STARTED)
    }

    fun clickGetStartedIfVisible() {
        if (isWelcomeScreenVisible()) {
            clickGetStarted()
        }
    }

    fun clickGetStarted() = clickOnTag(TestTags.V2.Welcome.BTN_GET_STARTED)
    fun clickHowWeHandleData() = clickOnTag(TestTags.V2.Welcome.BTN_HOW_WE_HANDLE_DATA)
}
