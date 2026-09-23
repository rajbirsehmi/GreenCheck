package com.creative.greencheck.testing.robots

import com.creative.greencheck.testing.TestTags
import com.sehmi.engine.actions.clickOnTag
import com.sehmi.engine.assertions.assertTagDisplayed
import com.sehmi.engine.core.ComposeRuleScope

class RobotHistoryScreen : ComposeRuleScope {
    fun verifyHistoryScreen() {
        assertTagDisplayed(TestTags.V2.History.SCREEN)
        assertTagDisplayed(TestTags.V2.History.LIST)
    }

    fun verifyClearButton() {
        assertTagDisplayed(TestTags.V2.History.CLEAR_CONTAINER)
        assertTagDisplayed(TestTags.V2.History.BTN_CLEAR)
    }

    fun clickClearHistory() {
        clickOnTag(TestTags.V2.History.BTN_CLEAR)
    }

    fun clickProductItem(barcode: String) {
        clickOnTag(TestTags.V2.Components.ProductItem.container(barcode))
    }
}
