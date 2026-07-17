package com.creative.isitvegan.domain.repo

import com.creative.isitvegan.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface Repository {
    suspend fun getProduct(barcode: String): Result<Product>
    suspend fun saveProduct(product: Product)
    suspend fun getProductFromDb(barcode: String): Product?
    fun getAllProducts(): Flow<List<Product>>
    suspend fun deleteProduct(product: Product)
}
