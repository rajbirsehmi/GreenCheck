package com.creative.isitvegan.testing.robots

import com.creative.isitvegan.testing.TestTags
import com.sehmi.engine.actions.clickOnTag
import com.sehmi.engine.assertions.assertTagDisplayed
import com.sehmi.engine.core.ComposeRuleScope

class RobotHomeScreen : ComposeRuleScope {
    fun verifyHeroSection() {
        assertTagDisplayed(TestTags.V2.Home.HERO_SECTION)
        assertTagDisplayed(TestTags.V2.Home.LOGO)
        assertTagDisplayed(TestTags.V2.Home.TITLE)
        assertTagDisplayed(TestTags.V2.Home.SUBTITLE)
    }

    fun verifyQuickActions() {
        assertTagDisplayed(TestTags.V2.Home.QUICK_ACTIONS_TITLE)
        assertTagDisplayed(TestTags.V2.Home.CARD_SCANNER)
        assertTagDisplayed(TestTags.V2.Home.CARD_SEARCH)
        assertTagDisplayed(TestTags.V2.Home.CARD_MANUAL)
        assertTagDisplayed(TestTags.V2.Home.CARD_HISTORY)
    }

    fun verifyInfoSections() {
        assertTagDisplayed(TestTags.V2.Home.SECTION_HOW_IT_WORKS)
        assertTagDisplayed(TestTags.V2.Home.SECTION_DISCLAIMER)
        assertTagDisplayed(TestTags.V2.Home.infoSectionTitle("How it works"))
        assertTagDisplayed(TestTags.V2.Home.infoSectionContent("How it works"))
        assertTagDisplayed(TestTags.V2.Home.infoSectionTitle("Disclaimer"))
        assertTagDisplayed(TestTags.V2.Home.infoSectionContent("Disclaimer"))
    }

    fun clickScanner() = clickOnTag(TestTags.V2.Home.CARD_SCANNER)
    fun clickSearch() = clickOnTag(TestTags.V2.Home.CARD_SEARCH)
    fun clickManual() = clickOnTag(TestTags.V2.Home.CARD_MANUAL)
    fun clickHistory() = clickOnTag(TestTags.V2.Home.CARD_HISTORY)
}
