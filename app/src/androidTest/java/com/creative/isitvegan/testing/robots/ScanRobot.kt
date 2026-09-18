package com.creative.isitvegan.testing.robots

import com.creative.isitvegan.testing.TestTags
import com.sehmi.engine.actions.clickOnTag
import com.sehmi.engine.assertions.assertTagDisplayed
import com.sehmi.engine.core.ComposeRuleScope

class ScanRobot : ComposeRuleScope {

    fun verifyScanScreenDisplayed() {
        assertTagDisplayed(TestTags.Scan.TEXT_INSTRUCTION)
        assertTagDisplayed(TestTags.Scan.BTN_CLOSE)
    }

    fun tapCloseButton() {
        clickOnTag(TestTags.Scan.BTN_CLOSE)
    }
}
