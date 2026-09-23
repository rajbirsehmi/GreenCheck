package com.creative.isitvegan.data.repo

import android.util.Log
import com.creative.isitvegan.data.local.AppDatabase
import com.creative.isitvegan.data.mapper.toDomain
import com.creative.isitvegan.data.mapper.toEntity
import com.creative.isitvegan.data.remote.OpenFoodFactsApi
import com.creative.isitvegan.domain.model.Product
import com.creative.isitvegan.domain.repo.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RepositoryImpl @Inject constructor(
    private val api: OpenFoodFactsApi,
    private val database: AppDatabase
) : Repository {

    private val TAG = "RepositoryImpl"

    override suspend fun getProduct(barcode: String): Result<Product> {
        return try {
            val response = api.getProduct(barcode)
            if (response.isFound && response.product != null) {
                Result.success(response.product.toDomain())
            } else if (response.status == 503 || response.statusVerbose?.contains("503") == true) {
                Result.failure(Exception("Searching too frequently. Please wait a moment and try again later."))
            } else {
                Result.failure(Exception(response.statusVerbose ?: "Product not found"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching product $barcode: ${e.message}")
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

    private fun mapException(e: Exception): Throwable {
        if (e is HttpException && e.code() == 503) {
            return Exception("Searching too frequently. Please wait a moment and try again later.")
        }
        if (e.message?.contains("503") == true) {
            return Exception("Searching too frequently. Please wait a moment and try again later.")
        }
        return e
    }

    override suspend fun saveProduct(product: Product) {
        database.productDao().insertProduct(product.toEntity())
    }

    override suspend fun getProductFromDb(barcode: String): Product? {
        return database.productDao().getProductByBarcode(barcode)?.toDomain()
    }

    override fun getAllProducts(): Flow<List<Product>> {
        return database.productDao().getAllProducts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun deleteProduct(product: Product) {
        database.productDao().deleteProduct(product.toEntity())
    }

    override suspend fun clearHistory() {
        database.productDao().deleteAllProducts()
    }
}
