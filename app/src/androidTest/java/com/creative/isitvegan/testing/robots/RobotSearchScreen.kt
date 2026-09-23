package com.creative.isitvegan.testing.robots

import androidx.compose.ui.test.hasTestTag
import com.creative.isitvegan.testing.TestTags
import com.sehmi.engine.actions.clickOnTag
import com.sehmi.engine.actions.enterText
import com.sehmi.engine.assertions.assertTagDisplayed
import com.sehmi.engine.core.ComposeRuleScope
import com.sehmi.engine.utils.waitUntilExists

class RobotSearchScreen : ComposeRuleScope {
    fun waitForResults() {
        waitUntilExists(hasTestTag(TestTags.V2.Search.LIST))
    }

    fun verifySearchScreen() {
        assertTagDisplayed(TestTags.V2.Search.LIST)
        assertTagDisplayed(TestTags.V2.Search.HEADER_SECTION)
        assertTagDisplayed(TestTags.V2.Search.TITLE)
    }

    fun verifySearchModes() {
        assertTagDisplayed(TestTags.V2.Search.MODE_SELECTION_ROW)
        assertTagDisplayed(TestTags.V2.Search.MODE_INGREDIENTS)
    }

    fun selectModeProducts() = clickOnTag(TestTags.V2.Search.MODE_PRODUCTS)
    fun selectModeIngredients() = clickOnTag(TestTags.V2.Search.MODE_INGREDIENTS)

    fun enterSearchQuery(query: String) {
        enterText(TestTags.V2.Search.TEXT_FIELD, query)
    }

    fun clickSearch() = clickOnTag(TestTags.V2.Search.BTN_INITIALIZE)

    fun verifyLoading() = assertTagDisplayed(TestTags.V2.Search.LOADING_INDICATOR)
    
    fun verifyNoResults() = assertTagDisplayed(TestTags.V2.Search.NO_RESULTS_TEXT)
    
    fun verifyError() = assertTagDisplayed(TestTags.V2.Search.ERROR_TEXT)
    
    fun verifyQuota() = assertTagDisplayed(TestTags.V2.Search.QUOTA_TEXT)

    fun clickProductItem(barcode: String) {
        clickOnTag(TestTags.V2.Components.ProductItem.container(barcode))
    }
}
