package com.creative.greencheck.data.repo

import android.util.Log
import com.creative.greencheck.data.local.AppDatabase
import com.creative.greencheck.data.remote.OpenFoodFactsApi
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
    fun `getProduct 429 HTTP exception returns searching too frequently error message`() = runBlocking {
        val httpException = HttpException(Response.error<Any>(429, "".toResponseBody(null)))
        coEvery { api.getProduct(any(), any()) } throws httpException

        val result = repository.getProduct("123456")

        assertTrue(result.isFailure)
        assertEquals("Searching too frequently. Please wait a moment and try again later.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `searchProducts 503 HTTP exception returns service unavailable error message`() = runBlocking {
        val httpException = HttpException(Response.error<Any>(503, "".toResponseBody(null)))
        coEvery { api.searchProducts(any(), any()) } throws httpException

        val result = repository.searchProducts("oat")

        assertTrue(result.isFailure)
        assertEquals("Service is temporarily unavailable. Please try again in a moment.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `searchByIngredient 503 HTTP exception returns service unavailable error message`() = runBlocking {
        val httpException = HttpException(Response.error<Any>(503, "".toResponseBody(null)))
        coEvery { api.searchByIngredient(any(), any()) } throws httpException

        val result = repository.searchByIngredient("milk")

        assertTrue(result.isFailure)
        assertEquals("Service is temporarily unavailable. Please try again in a moment.", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getVeganAlternatives 429 HTTP exception returns searching too frequently error message`() = runBlocking {
        val httpException = HttpException(Response.error<Any>(429, "".toResponseBody(null)))
        coEvery { api.searchVeganAlternatives(any(), any(), any(), any()) } throws httpException

        val result = repository.getVeganAlternatives(category = "chocolates")

        assertTrue(result.isFailure)
        assertEquals("Searching too frequently. Please wait a moment and try again later.", result.exceptionOrNull()?.message)
    }
}
