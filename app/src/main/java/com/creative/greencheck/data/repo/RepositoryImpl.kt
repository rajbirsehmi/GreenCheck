package com.creative.greencheck.data.repo

import android.util.Log
import com.creative.greencheck.data.local.AppDatabase
import com.creative.greencheck.data.mapper.toDomain
import com.creative.greencheck.data.mapper.toEntity
import com.creative.greencheck.data.remote.LocaleDetector
import com.creative.greencheck.data.remote.OpenFoodFactsApi
import com.creative.greencheck.domain.model.Product
import com.creative.greencheck.domain.repo.Repository
import com.creative.greencheck.util.normalizeBarcode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

class BarcodeNotFoundException(message: String) : Exception(message)

@Singleton
class RepositoryImpl @Inject constructor(
    private val api: OpenFoodFactsApi,
    private val database: AppDatabase
) : Repository {

    private val TAG = "RepositoryImpl"

    override suspend fun getProduct(barcode: String): Result<Product> {
        val normalized = barcode.normalizeBarcode()
        return try {
            val response = api.getProduct(normalized)
            if (response.isFound && response.product != null) {
                val domainProduct: Product = response.product.toDomain()
                val finalProduct = if (domainProduct.barcode.isBlank()) {
                    domainProduct.copy(barcode = normalized)
                } else domainProduct
                Result.success(finalProduct)
            } else if (response.status == 0) {
                Result.failure(BarcodeNotFoundException(response.statusVerbose ?: "Product not found"))
            } else if (response.status == 429 || response.statusVerbose?.contains("429") == true) {
                Result.failure(Exception("Searching too frequently. Please wait a moment and try again later."))
            } else if (response.status == 503 || response.statusVerbose?.contains("503") == true) {
                Result.failure(Exception("Service is temporarily unavailable. Please try again in a moment."))
            } else {
                Result.failure(Exception(response.statusVerbose ?: "Product not found"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching product $normalized: ${e.message}")
            if (e.message?.contains("404") == true)
                   Result.failure(BarcodeNotFoundException("Product not found"))
            else
                Result.failure(mapException(e))
        }
    }

    override suspend fun searchProducts(query: String): Result<List<Product>> {
        return try {
            val response = api.searchProducts(query)
            val products = response.products?.map { it.toDomain() } ?: emptyList()
            Result.success(products)
        } catch (e: Exception) {
            Log.e(TAG, "Error searching products for $query: ${e.message}")
            Result.failure(mapException(e))
        }
    }

    override suspend fun searchByIngredient(ingredient: String): Result<List<Product>> {
        return try {
            val response = api.searchByIngredient(ingredient)
            val products = response.products?.map { it.toDomain() } ?: emptyList()
            Result.success(products)
        } catch (e: Exception) {
            Log.e(TAG, "Error searching by ingredient $ingredient: ${e.message}")
            Result.failure(mapException(e))
        }
    }

    override suspend fun getVeganAlternatives(
        category: String?,
        searchQuery: String?,
        countryTag: String?
    ): Result<List<Product>> {
        return try {
            val formattedCategory = category?.lowercase()?.trim()
                ?.replace(Regex("[^a-z0-9\\-]"), "-")
                ?.replace(Regex("-+"), "-")
                ?.removeSurrounding("-")
                ?.let { if (it.startsWith("en:")) it else "en:$it" }

            val effectiveCountryTag = countryTag ?: LocaleDetector.currentDeviceCountryTag

            val response = api.searchVeganAlternatives(
                category = formattedCategory,
                searchTerms = searchQuery,
                countryTag = effectiveCountryTag
            )
            var products = response.products
                ?.map { it.toDomain() }
                ?.filter { it.isVegan } ?: emptyList()

            // Fallback: If category tag search yielded no results and searchQuery wasn't explicitly supplied,
            // try searching by using the raw category name as search terms.
            if (products.isEmpty() && searchQuery == null && !category.isNullOrBlank()) {
                val fallbackResponse = api.searchVeganAlternatives(
                    category = null,
                    searchTerms = category,
                    countryTag = effectiveCountryTag
                )
                products = fallbackResponse.products
                    ?.map { it.toDomain() }
                    ?.filter { it.isVegan } ?: emptyList()
            }

            Result.success(products)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching vegan alternatives: ${e.message}")
            Result.failure(mapException(e))
        }
    }

    private fun mapException(e: Exception): Throwable {
        if (e is HttpException) {
            return when (e.code()) {
                429 -> Exception("Searching too frequently. Please wait a moment and try again later.")
                503 -> Exception("Service is temporarily unavailable. Please try again in a moment.")
                else -> Exception("HTTP error ${e.code()}: ${e.message()}")
            }
        }
        if (e.message?.contains("429") == true) {
            return Exception("Searching too frequently. Please wait a moment and try again later.")
        }
        if (e.message?.contains("503") == true) {
            return Exception("Service is temporarily unavailable. Please try again in a moment.")
        }
        return e
    }

    override suspend fun saveProduct(product: Product) {
        val normalizedProduct = product.copy(barcode = product.barcode.normalizeBarcode())
        database.productDao().insertProduct(normalizedProduct.toEntity())
    }

    override suspend fun getProductFromDb(barcode: String): Product? {
        val normalized = barcode.normalizeBarcode()
        var entity = database.productDao().getProductByBarcode(normalized)
        if (entity == null && barcode.isNotBlank() && barcode != normalized) {
            // Migration check: If stored under legacy un-padded barcode, migrate it to normalized 14-digit format
            entity = database.productDao().getProductByBarcode(barcode)
            if (entity != null) {
                database.productDao().deleteProduct(entity)
                val updatedEntity = entity.copy(barcode = normalized)
                database.productDao().insertProduct(updatedEntity)
                entity = updatedEntity
            }
        }
        return entity?.toDomain()
    }

    override fun getAllProducts(): Flow<List<Product>> {
        return database.productDao().getAllProducts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun deleteProduct(product: Product) {
        val normalizedProduct = product.copy(barcode = product.barcode.normalizeBarcode())
        database.productDao().deleteProduct(normalizedProduct.toEntity())
    }

    override suspend fun clearHistory() {
        database.productDao().deleteAllProducts()
    }
}
