package com.creative.isitvegan.testing.robots

import com.creative.isitvegan.testing.TestTags
import com.sehmi.engine.actions.clickOnTag
import com.sehmi.engine.assertions.assertTagDisplayed
import com.sehmi.engine.core.ComposeRuleScope

class HomeRobot : ComposeRuleScope {

    fun verifyEmptyState() {
        assertTagDisplayed(TestTags.Home.TEXT_MAIN)
        assertTagDisplayed(TestTags.Home.TEXT_SUB_MAIN)
        assertTagDisplayed(TestTags.Home.BTN_SCAN)
    }

    fun verifyRecentScansTitleDisplayed() {
        assertTagDisplayed(TestTags.Home.TOP_BAR_TITLE)
    }

    fun tapScanButton() {
        clickOnTag(TestTags.Home.BTN_SCAN)
    }

    fun verifyProductInRecentList(barcode: String) {
        assertTagDisplayed(TestTags.Home.recentItem(barcode))
    }

    fun tapProductInRecentList(barcode: String) {
        clickOnTag(TestTags.Home.recentItem(barcode))
    }

    fun verifyRecentListDisplayed() {
        assertTagDisplayed(TestTags.Home.RECENT_LIST)
    }
}
