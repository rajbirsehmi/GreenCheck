package com.creative.greencheck.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse(
    @SerialName("code") val code: String? = null,
    @SerialName("product") val product: ProductDetails? = null,
    @SerialName("status") val status: Int? = null,
    @SerialName("status_verbose") val statusVerbose: String? = null
) {
    /**
     * API status codes:
     * 1 or 200: Product found
     * 0: Product not found / barcode not found
     * null: Unexpected response
     */
    val isFound: Boolean get() = status == 1 || status == 200
}
