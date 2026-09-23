package com.creative.isitvegan.data.repo

import android.util.Log
import com.creative.isitvegan.data.local.AppDatabase
import com.creative.isitvegan.data.remote.OpenFoodFactsApi
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class RepositoryImplTest {

    private val api: OpenFoodFactsApi = mockk()
    private val database: AppDatabase = mockk(relaxed = true)
    private lateinit var repository: RepositoryImpl

    @Before
    fun setup() {
        mockkStatic(Log::class)
        coEvery { Log.e(any(), any()) } returns 0
        repository = RepositoryImpl(api, database)
    }

    @Test
    fun `getProduct 503 HTTP exception returns custom error message`() = runBlocking {
        val httpException = HttpException(Response.error<Any>(503, "".toResponseBody(null)))
        coEvery { api.getProduct(any(), any()) } throws httpException

        val result = repository.getProduct("123456")

        assertTrue(result.isFailure)
        assertEquals("Searching too frequently. Please wait a moment and try again later.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `searchProducts 503 HTTP exception returns custom error message`() = runBlocking {
        val httpException = HttpException(Response.error<Any>(503, "".toResponseBody(null)))
        coEvery { api.searchProducts(any(), any()) } throws httpException

        val result = repository.searchProducts("oat")

        assertTrue(result.isFailure)
        assertEquals("Searching too frequently. Please wait a moment and try again later.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `searchByIngredient 503 HTTP exception returns custom error message`() = runBlocking {
        val httpException = HttpException(Response.error<Any>(503, "".toResponseBody(null)))
        coEvery { api.searchByIngredient(any(), any()) } throws httpException

        val result = repository.searchByIngredient("milk")

        assertTrue(result.isFailure)
        assertEquals("Searching too frequently. Please wait a moment and try again later.", result.exceptionOrNull()?.message)
    }
}
