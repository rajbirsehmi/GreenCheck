package com.creative.greencheck.ui.screens.v2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.creative.greencheck.domain.model.Product
import com.creative.greencheck.testing.TestTags
import com.creative.greencheck.ui.components.v2.ProductItem
import com.creative.greencheck.ui.viewmodels.SearchViewModel

@Composable
fun SearchProductOrIngredientScreen(
    onProductClick: (Product) -> Unit = {},
    onQuotaExhausted: (String) -> Unit = {},
    viewModel: SearchViewModel = hiltViewModel()
) {
    val quotaExhausted by viewModel.quotaExhausted.collectAsStateWithLifecycle()
    val remainingIngredient by viewModel.remainingIngredientSearches.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
    val currentQuery by viewModel.currentQuery.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(quotaExhausted) {
        quotaExhausted?.let { feature ->
            onQuotaExhausted(feature.name)
            viewModel.resetQuotaState()
        }
    }

    SearchProductOrIngredientContent(
        remainingIngredient = remainingIngredient,
        searchResults = searchResults,
        currentQuery = currentQuery,
        isLoading = isLoading,
        onSearch = viewModel::onSearchIngredient,
        onProductClick = { product ->
            viewModel.saveProduct(product)
            onProductClick(product)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchProductOrIngredientContent(
    remainingIngredient: Int,
    searchResults: List<Product>,
    currentQuery: String = "",
    isLoading: Boolean,
    onSearch: (String) -> Unit,
    onProductClick: (Product) -> Unit
) {
    var searchQuery by remember(currentQuery) { mutableStateOf(currentQuery) }
    var active by remember { mutableStateOf(false) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val handleSearch = {
        if (searchQuery.isNotBlank()) {
            keyboardController?.hide()
            active = false
            onSearch(searchQuery)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag(TestTags.V2.Search.LIST)
    ) {
        SearchBar(
            inputField = {
                SearchBarDefaults.InputField(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = { handleSearch() },
                    expanded = active,
                    onExpandedChange = { active = it },
                    placeholder = { Text("Search ingredients (e.g. Soy Milk)") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.testTag(TestTags.V2.Search.FIELD_ICON)
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(
                                onClick = {
                                    searchQuery = ""
                                    if (active) active = false
                                },
                                modifier = Modifier.testTag(TestTags.V2.Search.BTN_CLEAR_QUERY)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear Search",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                )
            },
            expanded = active,
            onExpandedChange = { active = it },
            modifier = Modifier
                .fillMaxWidth()
                .testTag(TestTags.V2.Search.TEXT_FIELD),
            colors = SearchBarDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        ) {
            // Search suggestions
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AssistChip(
                onClick = {},
                label = { Text("Ingredients") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    labelColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    leadingIconContentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ),
                modifier = Modifier.testTag(TestTags.V2.Search.FILTER_CHIP_INGREDIENTS)
            )

            Surface(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.testTag(TestTags.V2.Search.QUOTA_CONTAINER)
            ) {
                Text(
                    text = "Quota: $remainingIngredient / 10 left",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag(TestTags.V2.Search.QUOTA_TEXT)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.testTag(TestTags.V2.Search.LOADING_INDICATOR),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        } else if (searchResults.isNotEmpty()) {
            Text(
                text = "Results for \"${currentQuery.ifBlank { searchQuery }}\" (${searchResults.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(searchResults) { product ->
                    ProductItem(
                        product = product,
                        onClick = { onProductClick(product) }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        } else if (currentQuery.isNotBlank()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No ingredients found for \"$currentQuery\"",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.testTag(TestTags.V2.Search.NO_RESULTS_TEXT)
                )
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                            .testTag(TestTags.V2.Search.LOGO_CONTAINER),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier
                                .size(40.dp)
                                .testTag(TestTags.V2.Search.LOGO_ICON)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Search Database",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.testTag(TestTags.V2.Search.TITLE)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Type an ingredient name above to check vegan status.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.testTag(TestTags.V2.Search.SUBTITLE)
                    )
                }
            }
        }
    }
}
