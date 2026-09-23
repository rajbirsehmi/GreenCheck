package com.creative.isitvegan.testing.robots

import com.creative.isitvegan.testing.TestTags
import com.sehmi.engine.actions.clickOnTag
import com.sehmi.engine.assertions.assertTagDisplayed
import com.sehmi.engine.core.ComposeRuleScope

class RobotMainScaffolding: ComposeRuleScope {

    fun verifyThatTopAppBarShowsTheAppName() {
        assertTagDisplayed(TestTags.V2.Scaffolding.TOP_BAR_TITLE)
    }

    fun verifyBottomNav() {
        assertTagDisplayed(TestTags.V2.Scaffolding.BOTTOM_NAV_BAR)
        assertTagDisplayed(TestTags.V2.Scaffolding.navItem("home"))
        assertTagDisplayed(TestTags.V2.Scaffolding.navItem("manual"))
        assertTagDisplayed(TestTags.V2.Scaffolding.navItem("scanner"))
        assertTagDisplayed(TestTags.V2.Scaffolding.navItem("history"))
        assertTagDisplayed(TestTags.V2.Scaffolding.navItem("search"))
    }

    fun clickNavItem(route: String) = clickOnTag(TestTags.V2.Scaffolding.navItem(route))
    fun clickInfo() = clickOnTag(TestTags.V2.Scaffolding.BTN_OPEN_INFO)
}
