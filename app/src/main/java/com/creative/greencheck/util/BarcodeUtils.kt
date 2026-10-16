package com.creative.greencheck.util

/**
 * Normalizes a barcode string.
 * If the barcode length is less than 14 digits and contains only digits,
 * it pads the start with zeros ('0') up to 14 digits.
 */
fun String?.normalizeBarcode(): String {
    if (this == null) return ""
    val trimmed = this.trim()
    if (trimmed.isBlank()) return ""
    return if ((trimmed.length < 14) && trimmed.all { it.isDigit() }) {
        trimmed.padStart(14, '0')
    } else {
        trimmed
    }
}
