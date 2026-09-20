package com.creative.isitvegan.ui.screens.v2

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.creative.isitvegan.ui.theme.IsItVeganTheme
import com.creative.isitvegan.ui.viewmodels.SearchViewModel

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import com.creative.isitvegan.domain.model.Product
import com.creative.isitvegan.ui.components.v2.ProductItem

enum class SearchMode {
    PRODUCTS, INGREDIENTS
}

@Composable
fun SearchProductOrIngredientScreen(
    onProductClick: (Product) -> Unit = {},
    onQuotaExhausted: (String) -> Unit = {},
    viewModel: SearchViewModel = hiltViewModel()
) {
    var selectedMode by remember { mutableStateOf(SearchMode.PRODUCTS) }
    var searchQuery by remember { mutableStateOf("") }

    val quotaExhausted by viewModel.quotaExhausted.collectAsStateWithLifecycle()
    val remainingProduct by viewModel.remainingProductSearches.collectAsStateWithLifecycle()
    val remainingIngredient by viewModel.remainingIngredientSearches.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    LaunchedEffect(quotaExhausted) {
        quotaExhausted?.let { feature ->
            onQuotaExhausted(feature.name)
            viewModel.resetQuotaState()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("search_screen_list"),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp)
                    .testTag("search_header_section"),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 16.dp)
                        .testTag("search_logo_container"),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(48.dp)
                                .testTag("search_logo_icon")
                        )
                    }
                }
                Text(
                    text = "Deep Database Search",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.testTag("search_title")
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Explore our extensive library of products and individual ingredients.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .testTag("search_subtitle")
                )
            }
        }

        item {
            // Mode Selection
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .testTag("search_mode_selection_row"),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SearchSelectionCard(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("search_mode_products"),
                    title = "Products",
                    subtitle = "Browse Names",
                    icon = Icons.Default.ShoppingCart,
                    isSelected = selectedMode == SearchMode.PRODUCTS,
                    onClick = { selectedMode = SearchMode.PRODUCTS }
                )

                SearchSelectionCard(
                    modifier = Modifier
                        .weight(1f)
                        .testTag("search_mode_ingredients"),
                    title = "Ingredients",
                    subtitle = "Analyze Items",
                    icon = Icons.Default.Eco,
                    isSelected = selectedMode == SearchMode.INGREDIENTS,
                    onClick = { selectedMode = SearchMode.INGREDIENTS }
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
            // Search Input Area
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .testTag("search_input_card"),
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        label = { Text(if (selectedMode == SearchMode.PRODUCTS) "Search Products" else "Search Ingredients") },
                        placeholder = { Text("e.g. Soy Milk") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("search_text_field"),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            unfocusedContainerColor = Color.Transparent
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.testTag("search_field_icon")
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            if (selectedMode == SearchMode.PRODUCTS) {
                                viewModel.onSearchProduct(searchQuery)
                            } else {
                                viewModel.onSearchIngredient(searchQuery)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("button_initialize_search"),
                        shape = RoundedCornerShape(16.dp),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(24.dp)
                                    .testTag("search_loading_indicator"),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                modifier = Modifier.testTag("button_initialize_search_icon")
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = "Initialize Search",
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp,
                                modifier = Modifier.testTag("button_initialize_search_text")
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            // Quota Label
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .testTag("search_quota_container"),
                color = Color.Transparent
            ) {
                val remaining = if (selectedMode == SearchMode.PRODUCTS) remainingProduct else remainingIngredient
                val total = if (selectedMode == SearchMode.PRODUCTS) 10 else 15
                Text(
                    text = "Quota: $remaining of $total searches remaining",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search_quota_text"),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (error != null) {
            item {
                Text(
                    text = error ?: "An error occurred",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .testTag("search_error_text"),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        if (searchResults.isEmpty() && !isLoading && searchQuery.isNotBlank()) {
            item {
                Text(
                    text = "No products found for \"$searchQuery\"",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp)
                        .testTag("search_no_results_text"),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        items(searchResults) { product ->
            ProductItem(
                product = product,
                onClick = {
                    viewModel.saveProduct(product)
                    onProductClick(product)
                }
            )
        }
    }
}

@Composable
fun SearchSelectionCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier
            .height(130.dp)
            .testTag("search_selection_card_${title.lowercase()}"),
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected)
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
        else
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
        border = if (isSelected)
            BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
        else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp)
                .testTag("search_selection_card_content_${title.lowercase()}"),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier
                    .size(28.dp)
                    .testTag("search_selection_card_icon_${title.lowercase()}"),
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.testTag("search_selection_card_title_${title.lowercase()}")
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                modifier = Modifier.testTag("search_selection_card_subtitle_${title.lowercase()}")
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SearchProductOrIngredientScreenPreview() {
    IsItVeganTheme {
        SearchProductOrIngredientScreen()
    }
}
