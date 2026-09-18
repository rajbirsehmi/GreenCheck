package com.creative.isitvegan.testing.robots

import com.creative.isitvegan.testing.TestTags
import com.sehmi.engine.assertions.assertTagDisplayed
import com.sehmi.engine.core.ComposeRuleScope

class LoadingRobot : ComposeRuleScope {

    fun verifyLoadingState() {
        assertTagDisplayed(TestTags.Loading.ICON_ECO)
        assertTagDisplayed(TestTags.Loading.PROGRESS_BAR)
        assertTagDisplayed(TestTags.Loading.TEXT_MESSAGE)
    }
}
