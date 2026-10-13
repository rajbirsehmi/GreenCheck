package com.creative.greencheck.ui.viewmodels

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
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private lateinit var viewModel: SearchViewModel
    private val repository: Repository = mockk(relaxed = true)
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = SearchViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `onSearchProduct success updates searchResults`() = runTest {
        val product = Product(barcode = "12345", name = "Vegan Cookie")
        coEvery { repository.searchProducts("cookie") } returns Result.success(listOf(product))

        viewModel.onSearchProduct("cookie")
        advanceUntilIdle()

        assertEquals(1, viewModel.searchResults.value.size)
        assertEquals("Vegan Cookie", viewModel.searchResults.value.first().name)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `onSearchProduct failure sets error message`() = runTest {
        coEvery { repository.searchProducts("error") } returns Result.failure(Exception("Search failed"))

        viewModel.onSearchProduct("error")
        advanceUntilIdle()

        assertEquals(0, viewModel.searchResults.value.size)
        assertEquals("Search failed", viewModel.error.value)
    }

    @Test
    fun `onSearchIngredient success updates searchResults`() = runTest {
        val product = Product(barcode = "67890", name = "Almond Milk")
        coEvery { repository.searchByIngredient("almond") } returns Result.success(listOf(product))

        viewModel.onSearchIngredient("almond")
        advanceUntilIdle()

        assertEquals(1, viewModel.searchResults.value.size)
        assertEquals("Almond Milk", viewModel.searchResults.value.first().name)
        assertNull(viewModel.error.value)
    }

    @Test
    fun `onSearchIngredient failure sets error message`() = runTest {
        coEvery { repository.searchByIngredient("invalid") } returns Result.failure(Exception("Ingredient not found"))

        viewModel.onSearchIngredient("invalid")
        advanceUntilIdle()

        assertEquals(0, viewModel.searchResults.value.size)
        assertEquals("Ingredient not found", viewModel.error.value)
    }

    @Test
    fun `onSearchIngredient with blank query does not search`() = runTest {
        viewModel.onSearchIngredient("   ")
        advanceUntilIdle()

        coVerify(exactly = 0) { repository.searchByIngredient(any()) }
    }

    @Test
    fun `saveProduct calls repository saveProduct`() = runTest {
        val product = Product(barcode = "123", name = "Oat Milk")

        viewModel.saveProduct(product)
        advanceUntilIdle()

        coVerify { repository.saveProduct(product) }
    }
}
