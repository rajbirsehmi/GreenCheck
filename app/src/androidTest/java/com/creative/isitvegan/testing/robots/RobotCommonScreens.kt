package com.creative.isitvegan.testing.robots

import com.creative.isitvegan.testing.TestTags
import com.sehmi.engine.actions.clickOnTag
import com.sehmi.engine.assertions.assertTagDisplayed
import com.sehmi.engine.conditions.isDisplayed
import com.sehmi.engine.core.ComposeRuleScope
import com.sehmi.engine.utils.waitUntilDoesNotExist

class RobotErrorScreen : ComposeRuleScope {
    fun verifyErrorScreen() {
        assertTagDisplayed(TestTags.V2.Error.SCREEN)
        assertTagDisplayed(TestTags.V2.Error.TITLE)
    }

    fun clickReturnHome() = clickOnTag(TestTags.V2.Error.BTN_RETURN_HOME)
    fun clickRetry() = clickOnTag(TestTags.V2.Error.BTN_RETRY)

    fun isErrorVisible(): Boolean = isDisplayed(TestTags.V2.Error.SCREEN)
}

class RobotLoadingProductScreen : ComposeRuleScope {
    fun verifyLoadingScreen() {
        assertTagDisplayed(TestTags.V2.LoadingProduct.SCREEN)
        assertTagDisplayed(TestTags.V2.LoadingProduct.INDICATOR)
    }

    fun waitForLoadingFinish() {
        waitUntilDoesNotExist(TestTags.V2.LoadingProduct.SCREEN)
    }
}

class RobotQuotaExhaustedScreen : ComposeRuleScope {
    fun verifyQuotaExhaustedScreen() {
        assertTagDisplayed(TestTags.V2.QuotaExhausted.SCREEN)
        assertTagDisplayed(TestTags.V2.QuotaExhausted.TITLE)
    }

    fun clickUnderstood() = clickOnTag(TestTags.V2.QuotaExhausted.BTN_UNDERSTOOD)
}

class RobotEmptyHistoryScreen : ComposeRuleScope {
    fun verifyEmptyHistoryScreen() {
        assertTagDisplayed(TestTags.V2.EmptyHistory.SCREEN)
        assertTagDisplayed(TestTags.V2.EmptyHistory.TITLE)
    }
}
