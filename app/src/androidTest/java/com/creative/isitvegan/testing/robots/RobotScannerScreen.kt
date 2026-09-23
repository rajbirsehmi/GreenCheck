package com.creative.isitvegan.testing.robots

import com.creative.isitvegan.testing.TestTags
import com.sehmi.engine.actions.clickOnTag
import com.sehmi.engine.assertions.assertTagDisplayed
import com.sehmi.engine.core.ComposeRuleScope

class RobotScannerScreen : ComposeRuleScope {
    fun verifyScannerScreen() {
        assertTagDisplayed(TestTags.V2.Scanner.SCREEN_CONTAINER)
        assertTagDisplayed(TestTags.V2.Scanner.CAMERA_PREVIEW)
        assertTagDisplayed(TestTags.V2.Scanner.VIEWFINDER_WINDOW)
    }

    fun verifyOverlays() {
        assertTagDisplayed(TestTags.V2.Scanner.INSTRUCTIONS_OVERLAY)
        assertTagDisplayed(TestTags.V2.Scanner.STATUS_TEXT)
        assertTagDisplayed(TestTags.V2.Scanner.QUOTA_OVERLAY)
        assertTagDisplayed(TestTags.V2.Scanner.QUOTA_TEXT)
    }

    fun clickToggleVibration() = clickOnTag(TestTags.V2.Scanner.BTN_TOGGLE_VIBRATION)
}
