package com.creative.greencheck.ui.navgraph

object Routes {
    const val HOME = "home"
    const val LOADING = "loading/{barcode}"
    const val PRODUCT = "product/{barcode}"
    const val ERROR = "error/{barcode}"
    const val SCAN = "scanning"
    const val MANUAL = "manual"

    fun getLoadingRoute(barcode: String) = "loading/$barcode"
    fun getProductRoute(barcode: String) = "product/$barcode"
    fun getErrorRoute(barcode: String) = "error/$barcode"
}