package com.creative.greencheck.ui.screens.v2

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.creative.greencheck.domain.model.Product
import com.creative.greencheck.testing.TestTags
import com.creative.greencheck.ui.components.v2.ProductItem
import com.creative.greencheck.ui.theme.IsItVeganTheme
import com.creative.greencheck.ui.theme.NonVeganStatusRed
import com.creative.greencheck.ui.theme.UncertainStatusYellow
import com.creative.greencheck.ui.theme.VeganStatusGreen
import com.creative.greencheck.ui.viewmodels.SearchViewModel

enum class SearchMode {
    INGREDIENTS
}

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
    val error by viewModel.error.collectAsStateWithLifecycle()

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
        error = error,
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
    error: String?,
    onSearch: (String) -> Unit,
    onProductClick: (Product) -> Unit
) {
    var selectedMode by remember { mutableStateOf(SearchMode.INGREDIENTS) }
    var searchQuery by remember(currentQuery) { mutableStateOf(currentQuery) }
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedProductDetails by remember { mutableStateOf<Product?>(null) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val handleSearch = {
        if (searchQuery.isNotBlank()) {
            keyboardController?.hide()
            onSearch(searchQuery)
            showBottomSheet = true
            selectedProductDetails = null
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag(TestTags.V2.Search.LIST),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            // Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp)
                    .testTag(TestTags.V2.Search.HEADER_SECTION),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 16.dp)
                        .testTag(TestTags.V2.Search.LOGO_CONTAINER),
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
                                .testTag(TestTags.V2.Search.LOGO_ICON)
                        )
                    }
                }
                Text(
                    text = "Deep Database Search",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.testTag(TestTags.V2.Search.TITLE)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Explore our extensive library of individual ingredients.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .testTag(TestTags.V2.Search.SUBTITLE)
                )
            }
        }

        item {
            // Mode Selection
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .testTag(TestTags.V2.Search.MODE_SELECTION_ROW),
                horizontalArrangement = Arrangement.Center
            ) {
                SearchSelectionCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(TestTags.V2.Search.MODE_INGREDIENTS),
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
                    .testTag(TestTags.V2.Search.INPUT_CARD),
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
                        label = { Text("Search Ingredients") },
                        placeholder = { Text("e.g. Soy Milk") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { handleSearch() }),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag(TestTags.V2.Search.TEXT_FIELD),
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
                                modifier = Modifier.testTag(TestTags.V2.Search.FIELD_ICON)
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = handleSearch,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag(TestTags.V2.Search.BTN_INITIALIZE),
                        shape = RoundedCornerShape(16.dp),
                        enabled = !isLoading && searchQuery.isNotBlank()
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(24.dp)
                                    .testTag(TestTags.V2.Search.LOADING_INDICATOR),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                modifier = Modifier.testTag(TestTags.V2.Search.BTN_INITIALIZE_ICON)
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = "Initialize Search",
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp,
                                modifier = Modifier.testTag(TestTags.V2.Search.BTN_INITIALIZE_TEXT)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        if (searchResults.isNotEmpty()) {
            item {
                Surface(
                    onClick = {
                        showBottomSheet = true
                        selectedProductDetails = null
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                        .testTag("BTN_VIEW_LAST_RESULTS"),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.List,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "View Search Results",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                                val queryDisplay = currentQuery.ifBlank { searchQuery }
                                if (queryDisplay.isNotBlank()) {
                                    Text(
                                        text = "For \"$queryDisplay\" (${searchResults.size} found)",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        item {
            // Quota Label
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .testTag(TestTags.V2.Search.QUOTA_CONTAINER),
                color = Color.Transparent
            ) {
                val remaining = remainingIngredient
                val total = 10
                Text(
                    text = "Quota: $remaining of $total searches remaining",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(TestTags.V2.Search.QUOTA_TEXT),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showBottomSheet) {
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
                selectedProductDetails = null
            },
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
        ) {
            if (selectedProductDetails != null) {
                ProductDetailBottomSheetContent(
                    product = selectedProductDetails!!,
                    onBackClick = { selectedProductDetails = null },
                    onOpenFullScreen = { product ->
                        onProductClick(product)
                    }
                )
            } else if (isLoading) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(36.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(48.dp)
                            .testTag(TestTags.V2.Search.LOADING_INDICATOR),
                        color = MaterialTheme.colorScheme.primary,
                        strokeWidth = 4.dp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Searching ingredients...",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val displayQuery = currentQuery.ifBlank { searchQuery }
                        Text(
                            text = if (displayQuery.isNotBlank()) "Results for \"$displayQuery\"" else "Search Results",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${searchResults.size} found",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    HorizontalDivider(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    if (error != null) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp)
                                .testTag(TestTags.V2.Search.ERROR_TEXT),
                            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.2f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.ErrorOutline,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = error,
                                    color = MaterialTheme.colorScheme.onErrorContainer,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    } else if (searchResults.isEmpty()) {
                        val displayQuery = currentQuery.ifBlank { searchQuery }
                        Text(
                            text = "No ingredients found for \"$displayQuery\"",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(40.dp)
                                .testTag(TestTags.V2.Search.NO_RESULTS_TEXT),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag(TestTags.V2.Search.LIST),
                            contentPadding = PaddingValues(bottom = 24.dp)
                        ) {
                            items(searchResults) { product ->
                                ProductItem(
                                    product = product,
                                    onClick = {
                                        selectedProductDetails = product
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductDetailBottomSheetContent(
    product: Product,
    onBackClick: () -> Unit,
    onOpenFullScreen: (Product) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to results",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Product Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val imageUrl = product.imageUrl?.takeIf { it.isNotBlank() }
                            ?: product.thumbUrl?.takeIf { it.isNotBlank() }

                        Surface(
                            modifier = Modifier.size(72.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                        ) {
                            if (imageUrl != null) {
                                AsyncImage(
                                    model = ImageRequest.Builder(LocalContext.current)
                                        .data(imageUrl)
                                        .crossfade(true)
                                        .build(),
                                    contentDescription = product.name,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(4.dp),
                                    contentScale = ContentScale.Fit
                                )
                            } else {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Eco,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(36.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = (product.brands ?: "Unknown Brand").uppercase(),
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = product.name ?: "Unnamed Product",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (product.barcode.isNotBlank()) {
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Barcode: ${product.barcode}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            item {
                val (backgroundColor, textColor, icon, label) = when {
                    product.isVegan -> StatusThemeInfo(
                        VeganStatusGreen.copy(alpha = 0.15f),
                        VeganStatusGreen,
                        Icons.Default.Eco,
                        product.statusText
                    )
                    product.isNonVegan -> StatusThemeInfo(
                        NonVeganStatusRed.copy(alpha = 0.15f),
                        NonVeganStatusRed,
                        Icons.Default.Warning,
                        product.statusText
                    )
                    else -> StatusThemeInfo(
                        UncertainStatusYellow.copy(alpha = 0.15f),
                        UncertainStatusYellow,
                        Icons.Default.Warning,
                        product.statusText
                    )
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = backgroundColor
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = textColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = label,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = textColor
                        )
                    }
                }
            }

            if (!product.ingredients.isNullOrEmpty()) {
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Ingredients",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                val ingredientList = product.ingredients.mapNotNull { it.text }
                                ingredientList.forEachIndexed { index, ingredient ->
                                    Text(
                                        text = "${index + 1}. $ingredient",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            } else if (!product.ingredientsAnalysisTags.isNullOrEmpty()) {
                item {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Ingredients Analysis",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                product.ingredientsAnalysisTags.forEach { tag ->
                                    val cleanedTag = tag.removePrefix("en:").replace('-', ' ')
                                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
                                    Text(
                                        text = "• $cleanedTag",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.padding(vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Button(
                    onClick = {
                        onOpenFullScreen(product)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "View Full Screen Page",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private data class StatusThemeInfo(
    val backgroundColor: Color,
    val textColor: Color,
    val icon: ImageVector,
    val label: String
)

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
            .testTag(TestTags.V2.Search.selectionCard(title)),
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
                .testTag(TestTags.V2.Search.selectionCardContent(title)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier
                    .size(28.dp)
                    .testTag(TestTags.V2.Search.selectionCardIcon(title)),
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.testTag(TestTags.V2.Search.selectionCardTitle(title))
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                modifier = Modifier.testTag(TestTags.V2.Search.selectionCardSubtitle(title))
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun SearchProductOrIngredientScreenPreview() {
    IsItVeganTheme {
        SearchProductOrIngredientContent(
            remainingIngredient = 12,
            searchResults = emptyList(),
            isLoading = false,
            error = null,
            onSearch = {},
            onProductClick = {}
        )
    }
}
