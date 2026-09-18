package com.creative.isitvegan.testing.robots

import com.creative.isitvegan.testing.TestTags
import com.sehmi.engine.actions.clickOnTag
import com.sehmi.engine.assertions.assertTagDisplayed
import com.sehmi.engine.assertions.assertTextContains
import com.sehmi.engine.core.ComposeRuleScope
import com.sehmi.engine.actions.scrollToTag

class ProductRobot : ComposeRuleScope {

    fun verifyProductDetails(name: String, brand: String) {
        assertTagDisplayed(TestTags.Product.TEXT_NAME)
        assertTextContains(TestTags.Product.TEXT_NAME, name)
        assertTagDisplayed(TestTags.Product.TEXT_BRAND)
        assertTextContains(TestTags.Product.TEXT_BRAND, brand)
    }

    fun verifyVeganStatus(status: String) {
        assertTagDisplayed(TestTags.Product.BANNER_STATUS)
        assertTextContains(TestTags.Product.TEXT_STATUS, status)
    }

    fun tapBackButton() {
        clickOnTag(TestTags.Product.BTN_BACK)
    }

    fun verifyIngredientVisible(ingredientName: String) {
        val tag = TestTags.Product.ingredientItem(ingredientName)
        scrollToTag(tag)
        assertTagDisplayed(tag)
    }
}
