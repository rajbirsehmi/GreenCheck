package com.creative.greencheck.testing.robots

import androidx.compose.ui.test.hasTestTag
import com.creative.greencheck.testing.TestTags
import com.sehmi.engine.actions.clickOnTag
import com.sehmi.engine.actions.scrollToTag
import com.sehmi.engine.assertions.assertTagDisplayed
import com.sehmi.engine.core.ComposeRuleScope
import com.sehmi.engine.utils.waitUntilExists

class RobotProductScreen : ComposeRuleScope {
    fun waitForProductLoaded() {
        waitUntilExists(hasTestTag(TestTags.V2.Product.HERO_SECTION))
    }

    fun verifyLoading() {
        assertTagDisplayed(TestTags.V2.Product.LOADING)
    }

    fun verifyProductHero() {
        assertTagDisplayed(TestTags.V2.Product.HERO_SECTION)
        assertTagDisplayed(TestTags.V2.Product.IMAGE)
        assertTagDisplayed(TestTags.V2.Product.BRAND_NAME)
        assertTagDisplayed(TestTags.V2.Product.NAME)
        assertTagDisplayed(TestTags.V2.Product.BARCODE)
    }

    fun verifyStatusBanner() {
        scrollToTag(TestTags.V2.Product.STATUS_BANNER)
        assertTagDisplayed(TestTags.V2.Product.STATUS_BANNER)
        assertTagDisplayed(TestTags.V2.Product.STATUS_TITLE)
    }

    fun verifyProductDetails() {
        scrollToTag(TestTags.V2.Product.DETAILS_CARD)
        assertTagDisplayed(TestTags.V2.Product.DETAILS_CARD)
        assertTagDisplayed(TestTags.V2.Product.detailRow("Quantity"))
        assertTagDisplayed(TestTags.V2.Product.detailRow("Eco-Score"))
    }

    fun verifyIngredientsAnalysis() {
        scrollToTag(TestTags.V2.Product.analysisCard("vegan"))
        assertTagDisplayed(TestTags.V2.Product.analysisCard("vegan"))
        assertTagDisplayed(TestTags.V2.Product.analysisCard("uncertain"))
        assertTagDisplayed(TestTags.V2.Product.analysisCard("non_vegan"))
    }

    fun verifyAllIngredients() {
        scrollToTag(TestTags.V2.Product.ALL_INGREDIENTS_CARD)
        assertTagDisplayed(TestTags.V2.Product.ALL_INGREDIENTS_CARD)
    }

    fun clickClose() {
        scrollToTag(TestTags.V2.Product.BTN_CLOSE)
        clickOnTag(TestTags.V2.Product.BTN_CLOSE)
    }
}
