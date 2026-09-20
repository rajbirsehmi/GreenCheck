package com.creative.isitvegan.data.remote

import com.creative.isitvegan.data.remote.dto.ProductResponse
import com.creative.isitvegan.data.remote.dto.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenFoodFactsApi {

    /**
     * Get product details by barcode.
     * Endpoint: https://world.openfoodfacts.net/api/v2/product/[barcode]
     */
    @GET("api/v2/product/{barcode}")
    suspend fun getProduct(
        @Path("barcode") barcode: String,
        @Query("fields") fields: String? = null // Optional: filter fields to reduce payload size
    ): ProductResponse

    /**
     * Search products by name.
     * Endpoint: https://world.openfoodfacts.net/api/v2/search?search_terms=<product_name>&page_size=20
     */
    @GET("api/v2/search")
    suspend fun searchProducts(
        @Query("search_terms") query: String,
        @Query("page_size") pageSize: Int = 20
    ): SearchResponse

    /**
     * Search products by ingredient.
     * Endpoint: https://world.openfoodfacts.net/api/v2/search?ingredients_tags_en=<ingredient_name>&page_size=25
     */
    @GET("api/v2/search")
    suspend fun searchByIngredient(
        @Query("ingredients_tags_en") ingredient: String,
        @Query("page_size") pageSize: Int = 25
    ): SearchResponse

    companion object {
        const val BASE_URL = "https://world.openfoodfacts.org/"
    }
}
