package com.creative.greencheck.testing.robots

import com.creative.greencheck.testing.TestTags
import com.sehmi.engine.actions.clickOnTag
import com.sehmi.engine.actions.enterText
import com.sehmi.engine.assertions.assertTagDisplayed
import com.sehmi.engine.core.ComposeRuleScope

class RobotManualEntryScreen : ComposeRuleScope {
    fun verifyManualEntryScreen() {
        assertTagDisplayed(TestTags.V2.ManualEntry.SCREEN)
        assertTagDisplayed(TestTags.V2.ManualEntry.TITLE)
        assertTagDisplayed(TestTags.V2.ManualEntry.INPUT_CARD)
    }

    fun enterBarcode(barcode: String) {
        enterText(TestTags.V2.ManualEntry.BARCODE_FIELD, barcode)
    }

    fun clickIdentify() = clickOnTag(TestTags.V2.ManualEntry.BTN_IDENTIFY)

    fun verifyQuota() = assertTagDisplayed(TestTags.V2.ManualEntry.QUOTA_TEXT)
}
