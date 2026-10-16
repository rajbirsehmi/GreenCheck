package com.creative.greencheck.data.remote

import org.junit.Assert.assertEquals
import org.junit.Test

class LocaleDetectorTest {

    @Test
    fun `countryTag maps known region codes correctly`() {
        assertEquals("en:india", LocaleDetector.countryTag("IN"))
        assertEquals("en:united-states", LocaleDetector.countryTag("us"))
        assertEquals("en:united-kingdom", LocaleDetector.countryTag("GB"))
        assertEquals("en:united-kingdom", LocaleDetector.countryTag("uk"))
        assertEquals("en:canada", LocaleDetector.countryTag("ca"))
        assertEquals("en:australia", LocaleDetector.countryTag("AU"))
        assertEquals("en:germany", LocaleDetector.countryTag("de"))
        assertEquals("en:france", LocaleDetector.countryTag("FR"))
        assertEquals("en:japan", LocaleDetector.countryTag("jp"))
        assertEquals("en:mexico", LocaleDetector.countryTag("MX"))
        assertEquals("en:brazil", LocaleDetector.countryTag("br"))
        assertEquals("en:italy", LocaleDetector.countryTag("IT"))
        assertEquals("en:spain", LocaleDetector.countryTag("es"))
        assertEquals("en:netherlands", LocaleDetector.countryTag("NL"))
        assertEquals("en:switzerland", LocaleDetector.countryTag("ch"))
        assertEquals("en:sweden", LocaleDetector.countryTag("SE"))
        assertEquals("en:new-zealand", LocaleDetector.countryTag("nz"))
    }

    @Test
    fun `countryTag falls back to generic tag for unmapped regions`() {
        assertEquals("en:sg", LocaleDetector.countryTag("SG"))
        assertEquals("en:kr", LocaleDetector.countryTag("kr"))
    }
}
