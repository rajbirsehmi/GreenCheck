package com.creative.isitvegan

import com.creative.isitvegan.domain.model.Product
import com.creative.isitvegan.domain.repo.Repository
import com.creative.isitvegan.ui.viewmodels.RecentViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecentViewModelTest {

    private lateinit var viewModel: RecentViewModel
    private val repository: Repository = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        // Redirect Dispatchers.Main to our test dispatcher
        Dispatchers.setMain(testDispatcher)
        viewModel = RecentViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getProduct success updates product`() = runTest {
        // Given
        val barcode = "123456"
        val expectedProduct = Product(
            barcode = barcode,
            name = "Vegan Milk",
            brands = "Vegan Brand",
            quantity = null,
            productType = null,
            keywords = null,
            categories = null,
            dataSources = null,
            ingredientsAnalysisTags = null,
            labelsTags = null,
            ecoScoreGrade = null,
            ecoScore = null,
            imageUrl = null,
            thumbUrl = null,
            ingredientsImageUrl = null,
            nutritionImageUrl = null,
            ingredients = null
        )
        coEvery { repository.getProduct(barcode) } returns Result.success(expectedProduct)

        // When
        viewModel.getProduct(barcode)
        advanceUntilIdle() // Wait for the coroutine to finish

        // Then
        assertEquals(expectedProduct, viewModel.product)
    }

    @Test
    fun `getProduct failure sets product to null`() = runTest {
        // Given
        val barcode = "123456"
        val exception = Exception("Network error")
        coEvery { repository.getProduct(barcode) } returns Result.failure(exception)

        // When
        viewModel.getProduct(barcode)
        advanceUntilIdle()

        // Then
        assertNull(viewModel.product)
    }
}
