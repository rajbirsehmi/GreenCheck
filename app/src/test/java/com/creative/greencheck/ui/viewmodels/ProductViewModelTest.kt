package com.creative.greencheck.ui.viewmodels

import com.creative.greencheck.data.local.FeatureType
import com.creative.greencheck.data.local.UsageManager
import com.creative.greencheck.domain.model.Product
import com.creative.greencheck.domain.repo.Repository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewModelTest {

    private lateinit var viewModel: ProductViewModel
    private val repository: Repository = mockk()
    private val usageManager: UsageManager = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { usageManager.canUseFeature(FeatureType.ALTERNATIVE_SEARCH) } returns true
        viewModel = ProductViewModel(repository, usageManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchVeganAlternatives success updates alternativesState to Success`() = runTest {
        val testProduct = Product(
            barcode = "111111",
            name = "Non-Vegan Milk",
            categories = "Dairy, Milks",
            ingredientsAnalysisTags = listOf("en:non-vegan")
        )

        val veganAlt = Product(
            barcode = "222222",
            name = "Oat Milk",
            ingredientsAnalysisTags = listOf("en:vegan")
        )

        coEvery { repository.getVeganAlternatives("Dairy", null) } returns Result.success(listOf(veganAlt))

        viewModel.fetchVeganAlternatives(testProduct)
        advanceUntilIdle()

        assertTrue(viewModel.alternativesState is AlternativesUiState.Success)
        val successState = viewModel.alternativesState as AlternativesUiState.Success
        assertEquals(1, successState.alternatives.size)
        assertEquals("Oat Milk", successState.alternatives.first().name)
    }

    @Test
    fun `getProduct success from db updates uiState to Success`() = runTest {
        val product = Product(barcode = "12345", name = "Local Tofu")
        coEvery { repository.getProductFromDb("12345") } returns product

        viewModel.getProduct("12345")
        advanceUntilIdle()

        assertTrue(viewModel.uiState is ProductUiState.Success)
        val successState = viewModel.uiState as ProductUiState.Success
        assertEquals("Local Tofu", successState.product.name)
    }

    @Test
    fun `getProduct success from api updates uiState to Success and saves to db`() = runTest {
        val product = Product(barcode = "67890", name = "Remote Soy Milk")
        coEvery { repository.getProductFromDb("67890") } returns null
        coEvery { repository.getProduct("67890") } returns Result.success(product)
        coEvery { repository.saveProduct(product) } returns Unit

        viewModel.getProduct("67890")
        advanceUntilIdle()

        assertTrue(viewModel.uiState is ProductUiState.Success)
        val successState = viewModel.uiState as ProductUiState.Success
        assertEquals("Remote Soy Milk", successState.product.name)
        coVerify { repository.saveProduct(product) }
    }

    @Test
    fun `saveProduct calls repository saveProduct`() = runTest {
        val product = Product(barcode = "111", name = "Saved Product")
        coEvery { repository.saveProduct(product) } returns Unit

        viewModel.saveProduct(product)
        advanceUntilIdle()

        coVerify { repository.saveProduct(product) }
    }
}
