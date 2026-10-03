package com.creative.greencheck.data.remote

import com.creative.greencheck.data.remote.dto.ProductResponse
import com.creative.greencheck.data.remote.dto.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenFoodFactsApi {

    /**
     * Get product details by barcode.
     * Endpoint: https://world.openfoodfacts.org/api/v2/product/{barcode}.json?fields=code,product_name,brands,image_url,image_front_url,ingredients_analysis_tags,ingredients,ingredients_text,categories_tags,labels_tags
     */
    @GET("api/v2/product/{barcode}.json")
    suspend fun getProduct(
        @Path("barcode") barcode: String,
        @Query("fields") fields: String? = PRODUCT_FIELDS
    ): ProductResponse

    /**
     * Search products by name.
     * Endpoint: https://world.openfoodfacts.org/api/v2/search?search_terms=<product_name>&page_size=20&fields=...
     */
    @GET("api/v2/search")
    suspend fun searchProducts(
        @Query("search_terms") query: String,
        @Query("page_size") pageSize: Int = 20,
        @Query("fields") fields: String? = PRODUCT_FIELDS
    ): SearchResponse

    /**
     * Search products by ingredient using the CGI search endpoint.
     * Endpoint: https://world.openfoodfacts.org/cgi/search.pl?action=process&tagtype_0=ingredients&tag_contains_0=contains&tag_0=<searched_ingredient>&page_size=25&json=true&fields=code,product_name,brands,image_url,image_front_url,ingredients_analysis_tags,ingredients,ingredients_text,categories_tags,labels_tags
     */
    @GET("cgi/search.pl")
    suspend fun searchByIngredient(
        @Query("tag_0") ingredient: String,
        @Query("action") action: String = "process",
        @Query("tagtype_0") tagtype0: String = "ingredients",
        @Query("tag_contains_0") tagContains0: String = "contains",
        @Query("page_size") pageSize: Int = 25,
        @Query("json") json: String = "true",
        @Query("fields") fields: String? = PRODUCT_FIELDS
    ): SearchResponse

    /**
     * Search vegan alternative products by category or search terms.
     * Endpoint: https://world.openfoodfacts.org/api/v2/search?categories_tags_en=<category>&ingredients_analysis_tags=en:vegan&page_size=15&fields=...
     */
    @GET("api/v2/search")
    suspend fun searchVeganAlternatives(
        @Query("categories_tags_en") category: String? = null,
        @Query("search_terms") searchTerms: String? = null,
        @Query("ingredients_analysis_tags") veganTag: String = "en:vegan",
        @Query("page_size") pageSize: Int = 15,
        @Query("fields") fields: String? = PRODUCT_FIELDS
    ): SearchResponse

    companion object {
        const val BASE_URL = "https://world.openfoodfacts.org/"
        const val PRODUCT_FIELDS = "code,product_name,brands,image_url,image_front_url,ingredients_analysis_tags,ingredients,ingredients_text,categories_tags,labels_tags"
    }
}

