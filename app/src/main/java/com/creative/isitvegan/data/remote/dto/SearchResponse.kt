package com.creative.isitvegan.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    @SerialName("count") val count: Int? = null,
    @SerialName("page") val page: Int? = null,
    @SerialName("page_count") val pageCount: Int? = null,
    @SerialName("page_size") val pageSize: Int? = null,
    @SerialName("products") val products: List<ProductDetails>? = null
)
