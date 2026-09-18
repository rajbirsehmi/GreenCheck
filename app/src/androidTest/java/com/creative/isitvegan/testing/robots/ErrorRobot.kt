package com.creative.isitvegan.testing.robots

import com.creative.isitvegan.testing.TestTags
import com.sehmi.engine.actions.clickOnTag
import com.sehmi.engine.assertions.assertTagDisplayed
import com.sehmi.engine.core.ComposeRuleScope

class ErrorRobot : ComposeRuleScope {

    fun verifyErrorDisplayed() {
        assertTagDisplayed(TestTags.Error.TEXT_TITLE)
        assertTagDisplayed(TestTags.Error.TEXT_DESC)
    }

    fun tapBackToHome() {
        clickOnTag(TestTags.Error.BTN_HOME)
    }
}
