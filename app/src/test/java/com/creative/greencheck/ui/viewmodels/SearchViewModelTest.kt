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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private lateinit var viewModel: SearchViewModel
    private val repository: Repository = mockk(relaxed = true)
    private val usageManager: UsageManager = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        coEvery { usageManager.getRemainingUsage(FeatureType.SEARCH_INGREDIENT) } returns flowOf(10)
        viewModel = SearchViewModel(repository, usageManager)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onSearchProduct success updates searchResults and increments usage`() = runTest {
        val product = Product(barcode = "12345", name = "Vegan Cookie")
        coEvery { usageManager.canUseFeature(FeatureType.SEARCH_INGREDIENT) } returns true
        coEvery { repository.searchProducts("cookie") } returns Result.success(listOf(product))

        viewModel.onSearchProduct("cookie")
        advanceUntilIdle()

        assertEquals(1, viewModel.searchResults.value.size)
        assertEquals("Vegan Cookie", viewModel.searchResults.value.first().name)
        assertNull(viewModel.error.value)
        coVerify { usageManager.incrementUsage(FeatureType.SEARCH_INGREDIENT) }
    }

    @Test
    fun `onSearchProduct failure sets error message`() = runTest {
        coEvery { usageManager.canUseFeature(FeatureType.SEARCH_INGREDIENT) } returns true
        coEvery { repository.searchProducts("error") } returns Result.failure(Exception("Search failed"))

        viewModel.onSearchProduct("error")
        advanceUntilIdle()

        assertEquals(0, viewModel.searchResults.value.size)
        assertEquals("Search failed", viewModel.error.value)
    }

    @Test
    fun `onSearchProduct quota exhausted sets quotaExhausted state`() = runTest {
        coEvery { usageManager.canUseFeature(FeatureType.SEARCH_INGREDIENT) } returns false

        viewModel.onSearchProduct("cookie")
        advanceUntilIdle()

        assertEquals(FeatureType.SEARCH_INGREDIENT, viewModel.quotaExhausted.value)
    }

    @Test
    fun `onSearchIngredient success updates searchResults and increments usage`() = runTest {
        val product = Product(barcode = "67890", name = "Almond Milk")
        coEvery { usageManager.canUseFeature(FeatureType.SEARCH_INGREDIENT) } returns true
        coEvery { repository.searchByIngredient("almond") } returns Result.success(listOf(product))

        viewModel.onSearchIngredient("almond")
        advanceUntilIdle()

        assertEquals(1, viewModel.searchResults.value.size)
        assertEquals("Almond Milk", viewModel.searchResults.value.first().name)
        assertNull(viewModel.error.value)
        coVerify { usageManager.incrementUsage(FeatureType.SEARCH_INGREDIENT) }
    }

    @Test
    fun `onSearchIngredient failure sets error message`() = runTest {
        coEvery { usageManager.canUseFeature(FeatureType.SEARCH_INGREDIENT) } returns true
        coEvery { repository.searchByIngredient("invalid") } returns Result.failure(Exception("Ingredient not found"))

        viewModel.onSearchIngredient("invalid")
        advanceUntilIdle()

        assertEquals(0, viewModel.searchResults.value.size)
        assertEquals("Ingredient not found", viewModel.error.value)
    }

    @Test
    fun `onSearchIngredient quota exhausted sets quotaExhausted state`() = runTest {
        coEvery { usageManager.canUseFeature(FeatureType.SEARCH_INGREDIENT) } returns false

        viewModel.onSearchIngredient("almond")
        advanceUntilIdle()

        assertEquals(FeatureType.SEARCH_INGREDIENT, viewModel.quotaExhausted.value)
    }

    @Test
    fun `onSearchIngredient with blank query does not search`() = runTest {
        viewModel.onSearchIngredient("   ")
        advanceUntilIdle()

        coVerify(exactly = 0) { repository.searchByIngredient(any()) }
    }

    @Test
    fun `resetQuotaState clears quotaExhausted value`() = runTest {
        coEvery { usageManager.canUseFeature(FeatureType.SEARCH_INGREDIENT) } returns false

        viewModel.onSearchIngredient("almond")
        advanceUntilIdle()

        assertEquals(FeatureType.SEARCH_INGREDIENT, viewModel.quotaExhausted.value)

        viewModel.resetQuotaState()
        assertNull(viewModel.quotaExhausted.value)
    }

    @Test
    fun `saveProduct calls repository saveProduct`() = runTest {
        val product = Product(barcode = "123", name = "Oat Milk")

        viewModel.saveProduct(product)
        advanceUntilIdle()

        coVerify { repository.saveProduct(product) }
    }
}
