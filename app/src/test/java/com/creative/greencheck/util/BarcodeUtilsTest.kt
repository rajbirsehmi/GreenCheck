package com.creative.greencheck.util

import org.junit.Assert.assertEquals
import org.junit.Test

class BarcodeUtilsTest {

    @Test
    fun `normalizeBarcode pads barcodes shorter than 14 digits with leading zeros`() {
        assertEquals("00016000264601", "016000264601".normalizeBarcode())
        assertEquals("00000012345678", "12345678".normalizeBarcode())
        assertEquals("00000000000001", "1".normalizeBarcode())
    }

    @Test
    fun `normalizeBarcode leaves 14-digit or longer barcodes unchanged`() {
        assertEquals("00016000264601", "00016000264601".normalizeBarcode())
        assertEquals("123456789012345", "123456789012345".normalizeBarcode())
    }

    @Test
    fun `normalizeBarcode handles null or blank input safely`() {
        assertEquals("", (null as String?).normalizeBarcode())
        assertEquals("", "".normalizeBarcode())
        assertEquals("", "   ".normalizeBarcode())
    }

    @Test
    fun `normalizeBarcode handles non-digit inputs without padding`() {
        assertEquals("ABC1234", "ABC1234".normalizeBarcode())
    }
}
