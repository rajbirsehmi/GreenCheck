package com.creative.greencheck.ui.screens.v2

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.creative.greencheck.domain.model.Ingredient
import com.creative.greencheck.domain.model.Product
import com.creative.greencheck.testing.TestTags
import com.creative.greencheck.ui.theme.IsItVeganTheme
import com.creative.greencheck.ui.theme.NonVeganStatusRed
import com.creative.greencheck.ui.theme.UncertainStatusYellow
import com.creative.greencheck.ui.theme.VeganStatusGreen
import com.creative.greencheck.ui.viewmodels.AlternativesUiState
import com.creative.greencheck.ui.viewmodels.ProductUiState
import com.creative.greencheck.ui.viewmodels.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    barcode: String,
    onCloseClick: () -> Unit = {},
    onProductClick: (Product) -> Unit = {},
    onQuotaExhausted: (String) -> Unit = {},
    viewModel: ProductViewModel = hiltViewModel()
) {
    val quotaExhausted by viewModel.quotaExhausted.collectAsStateWithLifecycle()

    LaunchedEffect(barcode) {
        viewModel.getProduct(barcode)
    }

    LaunchedEffect(quotaExhausted) {
        quotaExhausted?.let { feature ->
            onQuotaExhausted(feature.name)
            viewModel.resetQuotaState()
        }
    }

    val state = viewModel.uiState
    var selectedAlternative by remember { mutableStateOf<Product?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (state) {
            is ProductUiState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .testTag(TestTags.V2.Product.LOADING),
                    color = MaterialTheme.colorScheme.primary
                )
            }

            is ProductUiState.Success -> {
                ProductContent(
                    product = state.product,
                    alternativesState = viewModel.alternativesState,
                    onFetchAlternatives = { viewModel.fetchVeganAlternatives(state.product) },
                    onProductClick = { altProduct ->
                        viewModel.saveProduct(altProduct)
                        selectedAlternative = altProduct
                    },
                    onCloseClick = onCloseClick
                )
            }

            is ProductUiState.Error -> {
                ErrorState(
                    message = state.message,
                    onRetry = { viewModel.getProduct(barcode) }
                )
            }

            is ProductUiState.Empty -> {
                ErrorState(
                    message = "Product not found",
                    onRetry = { viewModel.getProduct(barcode) }
                )
            }
        }

        selectedAlternative?.let { altProduct ->
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
            ModalBottomSheet(
                onDismissRequest = { selectedAlternative = null },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                modifier = Modifier.testTag(TestTags.V2.Product.ALTERNATIVE_SHEET)
            ) {
                AlternativeProductDetailSheet(
                    product = altProduct,
                    onClose = { selectedAlternative = null },
                    onOpenFullScreen = { fullProduct ->
                        selectedAlternative = null
                        onProductClick(fullProduct)
                    }
                )
            }
        }
    }
}

@Composable
fun ProductContent(
    product: Product,
    alternativesState: AlternativesUiState = AlternativesUiState.Idle,
    onFetchAlternatives: () -> Unit = {},
    onProductClick: (Product) -> Unit = {},
    onCloseClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag(TestTags.V2.Product.CONTENT_LIST),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            ProductHeroSection(product)
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
            ProductStatusBanner(product)
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
            CategoriesAndLabelsSection(product)
        }

        if (product.isNonVegan) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                VeganAlternativesSection(
                    alternativesState = alternativesState,
                    onFetchAlternatives = onFetchAlternatives,
                    onProductClick = onProductClick
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            IngredientsAnalysisSection(product)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            DetailedIngredientsSection(product)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            AllIngredientsSection(product)
        }
    }
}

@Composable
fun ProductHeroSection(product: Product) {
    val imageUrl = product.imageUrl?.takeIf { it.isNotBlank() }
        ?: product.thumbUrl?.takeIf { it.isNotBlank() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(TestTags.V2.Product.HERO_SECTION)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clip(MaterialTheme.shapes.extraLarge)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .testTag(TestTags.V2.Product.IMAGE_CONTAINER),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Eco,
                contentDescription = "Default Product Icon",
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                modifier = Modifier
                    .size(64.dp)
                    .testTag(TestTags.V2.Product.IMAGE)
            )

            if (imageUrl != null) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .testTag(TestTags.V2.Product.IMAGE),
                    contentScale = ContentScale.Fit
                )
            }

            // Branding Overlay Badge
            Card(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .testTag(TestTags.V2.Product.BRAND_OVERLAY),
                shape = MaterialTheme.shapes.small,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Text(
                    text = (product.brands ?: "Unknown Brand").uppercase(),
                    modifier = Modifier
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                        .testTag(TestTags.V2.Product.BRAND_NAME),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .testTag(TestTags.V2.Product.TITLE_SECTION)
        ) {
            Text(
                text = product.name ?: "Unnamed Product",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.testTag(TestTags.V2.Product.NAME)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "UPC: ${product.barcode}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 0.5.sp,
                modifier = Modifier.testTag(TestTags.V2.Product.BARCODE)
            )
        }
    }
}

@Composable
fun ProductStatusBanner(product: Product) {
    val (statusColor, containerColor, onContainerColor, statusTitle, statusDesc) = when {
        product.isVegan -> Quintuple(
            VeganStatusGreen,
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.onPrimaryContainer,
            "VEGAN CERTIFIED",
            "Plant-based goodness. No animal derivatives detected."
        )

        product.isNonVegan -> Quintuple(
            NonVeganStatusRed,
            MaterialTheme.colorScheme.errorContainer,
            MaterialTheme.colorScheme.onErrorContainer,
            "NON-VEGAN",
            "Contains animal-derived ingredients."
        )

        else -> Quintuple(
            UncertainStatusYellow,
            MaterialTheme.colorScheme.tertiaryContainer,
            MaterialTheme.colorScheme.onTertiaryContainer,
            "UNCERTAIN STATUS",
            "Source of some ingredients could not be verified."
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .testTag(TestTags.V2.Product.STATUS_BANNER),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(statusColor.copy(alpha = 0.2f), CircleShape)
                    .testTag(TestTags.V2.Product.STATUS_ICON_CONTAINER),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .background(statusColor, CircleShape)
                        .testTag(TestTags.V2.Product.STATUS_DOT)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = statusTitle,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp,
                    color = onContainerColor,
                    modifier = Modifier.testTag(TestTags.V2.Product.STATUS_TITLE)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = statusDesc,
                    style = MaterialTheme.typography.bodyMedium,
                    color = onContainerColor.copy(alpha = 0.85f),
                    modifier = Modifier.testTag(TestTags.V2.Product.STATUS_DESCRIPTION)
                )
            }
        }
    }
}

@Composable
fun CategoriesAndLabelsSection(product: Product) {
    val labels = product.formattedLabels
    val categories = product.formattedCategories
    val palmOilStatus = product.palmOilStatusText

    if (labels.isEmpty() && categories.isEmpty() && palmOilStatus == null) return

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .testTag("categories_and_labels_section")
    ) {
        SectionTitle("Categories & Labels")
        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 2.dp),
            modifier = Modifier.testTag("categories_and_labels_list")
        ) {
            palmOilStatus?.let { status ->
                item {
                    val isFree = product.isPalmOilFree
                    val chipContainer = if (isFree) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.tertiaryContainer
                    val chipContent = if (isFree) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onTertiaryContainer
                    AssistChip(
                        onClick = {},
                        label = { Text(status, fontWeight = FontWeight.Bold) },
                        leadingIcon = {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(chipContent, CircleShape)
                            )
                        },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = chipContainer,
                            labelColor = chipContent
                        ),
                        modifier = Modifier.testTag("chip_palm_oil")
                    )
                }
            }

            items(labels) { label ->
                AssistChip(
                    onClick = {},
                    label = { Text(label, fontWeight = FontWeight.SemiBold) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        labelColor = MaterialTheme.colorScheme.onSecondaryContainer
                    ),
                    modifier = Modifier.testTag("chip_label_$label")
                )
            }

            items(categories) { category ->
                AssistChip(
                    onClick = {},
                    label = { Text(category) },
                    colors = AssistChipDefaults.assistChipColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.testTag("chip_category_$category")
                )
            }
        }
    }
}

@Composable
fun IngredientsAnalysisSection(product: Product) {
    val ingredients = product.ingredients ?: emptyList()
    val vegan = ingredients.filter { it.vegan == "yes" }.mapNotNull { it.text }
    val nonVegan = ingredients.filter { it.vegan == "no" }.mapNotNull { it.text }
    val uncertain =
        ingredients.filter { it.vegan != "yes" && it.vegan != "no" }.mapNotNull { it.text }

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .testTag(TestTags.V2.Product.INGREDIENTS_ANALYSIS_SECTION)
    ) {
        SectionTitle("Ingredients Analysis")
        Spacer(modifier = Modifier.height(10.dp))

        AnalysisCard("Vegan Friendly", VeganStatusGreen, vegan, "vegan")
        Spacer(modifier = Modifier.height(10.dp))
        AnalysisCard("Uncertain Source", UncertainStatusYellow, uncertain, "uncertain")
        Spacer(modifier = Modifier.height(10.dp))
        AnalysisCard("Non-Vegan Detected", NonVeganStatusRed, nonVegan, "non_vegan")
    }
}

@Composable
fun AnalysisCard(title: String, color: Color, items: List<String>, tagSuffix: String) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(TestTags.V2.Product.analysisCard(tagSuffix)),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.outlinedCardColors(
            containerColor = color.copy(alpha = 0.08f)
        ),
        border = BorderStroke(1.dp, color.copy(alpha = 0.25f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(color, CircleShape)
                        .testTag(TestTags.V2.Product.analysisCardDot(tagSuffix))
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = color,
                    modifier = Modifier.testTag(TestTags.V2.Product.analysisCardTitle(tagSuffix))
                )
            }
            if (items.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier = Modifier.testTag(TestTags.V2.Product.analysisCardItems(tagSuffix)),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier.testTag(
                                TestTags.V2.Product.analysisCardItem(
                                    tagSuffix,
                                    index
                                )
                            ),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "•",
                                style = MaterialTheme.typography.bodyMedium,
                                color = color.copy(alpha = 0.8f),
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = item.replaceFirstChar { c -> c.uppercase() },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.testTag(
                                    TestTags.V2.Product.analysisCardItemText(
                                        tagSuffix,
                                        index
                                    )
                                )
                            )
                        }
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "None detected",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.testTag(TestTags.V2.Product.analysisCardEmpty(tagSuffix))
                )
            }
        }
    }
}

@Composable
fun DetailedIngredientsSection(product: Product) {
    val ingredients = product.ingredients ?: return
    if (ingredients.isEmpty()) return

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .testTag("detailed_ingredients_section")
    ) {
        SectionTitle("Ingredients Breakdown")
        Spacer(modifier = Modifier.height(10.dp))

        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                ingredients.forEachIndexed { index, ingredient ->
                    IngredientDetailRow(ingredient = ingredient, index = index)
                    if (index < ingredients.lastIndex) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun IngredientDetailRow(ingredient: Ingredient, index: Int) {
    val statusColor = when {
        ingredient.isVegan -> VeganStatusGreen
        ingredient.isNonVegan -> NonVeganStatusRed
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    ListItem(
        modifier = Modifier.testTag("ingredient_detail_row_$index"),
        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
        leadingContent = {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(statusColor, CircleShape)
            )
        },
        headlineContent = {
            Text(
                text = (ingredient.text ?: "Unknown").replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        trailingContent = {
            ingredient.formattedPercentage?.let { pct ->
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    Text(
                        text = pct,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        },
        supportingContent = {
            ingredient.subIngredients?.takeIf { it.isNotEmpty() }?.let { subs ->
                Column(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    subs.forEach { sub ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "└ ",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                            Text(
                                text = (sub.text ?: "").replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            sub.formattedPercentage?.let { subPct ->
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "($subPct)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
fun AllIngredientsSection(product: Product) {
    val ingredients = product.ingredients?.mapNotNull { it.text } ?: emptyList()
    val rawIngredientsText = product.ingredientsText
    if (ingredients.isEmpty() && rawIngredientsText.isNullOrBlank()) return

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .testTag(TestTags.V2.Product.ALL_INGREDIENTS_SECTION)
    ) {
        SectionTitle("Full Ingredient List")
        Spacer(modifier = Modifier.height(10.dp))
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(TestTags.V2.Product.ALL_INGREDIENTS_CARD),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .testTag(TestTags.V2.Product.ALL_INGREDIENTS_LIST),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (ingredients.isNotEmpty()) {
                    ingredients.forEachIndexed { index, ingredient ->
                        Row(
                            modifier = Modifier.testTag(TestTags.V2.Product.allIngredientsItem(index)),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "•",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = ingredient.replaceFirstChar { c -> c.uppercase() },
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.testTag(
                                    TestTags.V2.Product.allIngredientsItemText(
                                        index
                                    )
                                )
                            )
                        }
                    }
                } else if (!rawIngredientsText.isNullOrBlank()) {
                    Text(
                        text = rawIngredientsText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title.uppercase(),
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.2.sp,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.testTag(TestTags.V2.Product.sectionTitle(title))
    )
}

@Composable
fun ErrorState(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .testTag(TestTags.V2.Product.ERROR_STATE_CONTAINER),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Warning,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier
                .size(64.dp)
                .testTag(TestTags.V2.Product.ERROR_STATE_ICON)
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "An unexpected error occurred",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.testTag(TestTags.V2.Product.ERROR_STATE_TITLE)
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 8.dp)
                .testTag(TestTags.V2.Product.ERROR_STATE_MESSAGE)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onRetry,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.testTag(TestTags.V2.Product.BTN_ERROR_RETRY)
        ) {
            Icon(
                Icons.Default.Refresh,
                contentDescription = null,
                modifier = Modifier.testTag(TestTags.V2.Product.BTN_ERROR_RETRY_ICON)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "Try Again",
                modifier = Modifier.testTag(TestTags.V2.Product.BTN_ERROR_RETRY_TEXT)
            )
        }
    }
}

@Composable
fun VeganAlternativesSection(
    alternativesState: AlternativesUiState,
    onFetchAlternatives: () -> Unit,
    onProductClick: (Product) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .testTag("vegan_alternatives_section")
    ) {
        SectionTitle("Plant-Based Alternatives")
        Spacer(modifier = Modifier.height(10.dp))

        when (alternativesState) {
            is AlternativesUiState.Idle -> {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Looking for a vegan option?",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Discover certified vegan alternatives in this category.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.8f)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Button(
                            onClick = onFetchAlternatives,
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Icon(
                                imageVector = Icons.Default.Eco,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Find", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            is AlternativesUiState.Loading -> {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(20.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Finding vegan alternatives...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            is AlternativesUiState.Success -> {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(alternativesState.alternatives) { altProduct ->
                        AlternativeProductItem(
                            product = altProduct,
                            onClick = { onProductClick(altProduct) }
                        )
                    }
                }
            }

            is AlternativesUiState.Error -> {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = alternativesState.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = onFetchAlternatives,
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AlternativeProductItem(
    product: Product,
    onClick: () -> Unit = {}
) {
    val imageUrl = product.imageUrl?.takeIf { it.isNotBlank() }
        ?: product.thumbUrl?.takeIf { it.isNotBlank() }

    ElevatedCard(
        modifier = Modifier
            .width(160.dp)
            .clickable { onClick() }
            .testTag("alternative_product_item_${product.barcode}"),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        MaterialTheme.colorScheme.primaryContainer,
                        MaterialTheme.shapes.small
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (imageUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = product.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = product.name ?: "Vegan Product",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                color = MaterialTheme.colorScheme.onSurface
            )

            if (!product.brands.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = product.brands,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = MaterialTheme.shapes.extraSmall
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(VeganStatusGreen, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Vegan",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
fun AlternativeProductDetailSheet(
    product: Product,
    onClose: () -> Unit,
    onOpenFullScreen: (Product) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("alternative_detail_sheet_content"),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            ProductHeroSection(product)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            ProductStatusBanner(product)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            CategoriesAndLabelsSection(product)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            IngredientsAnalysisSection(product)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            DetailedIngredientsSection(product)
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
            AllIngredientsSection(product)
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onClose,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("btn_close_alternative_sheet"),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = "Close Preview",
                        fontWeight = FontWeight.Bold
                    )
                }

                Button(
                    onClick = {
                        onClose()
                        onOpenFullScreen(product)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .testTag("btn_open_full_screen_alternative"),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = "Full Page",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

private data class Quintuple<A, B, C, D, E>(val first: A, val second: B, val third: C, val fourth: D, val fifth: E)

@Composable
@Preview(showBackground = true)
fun ProductScreenPreview() {
    IsItVeganTheme {
        ProductContent(
            product = Product(
                barcode = "5711953184871",
                name = "Protein Drink",
                brands = "Starbucks",
                categoriesTags = listOf("en:beverages", "en:dairies", "en:coffee-drinks", "en:protein-drink"),
                labelsTags = listOf("en:no-added-sugar"),
                ingredientsAnalysisTags = listOf("en:palm-oil-free", "en:non-vegan"),
                imageUrl = "https://images.openfoodfacts.org/images/products/571/195/318/4871/front_en.12.400.jpg",
                ingredients = listOf(
                    Ingredient(text = "fat milk", percent = 1.2, percentEstimate = 58.62),
                    Ingredient(text = "Starbucks Arabica coffee coffee extract", percent = 20.9),
                    Ingredient(text = "milk protein", percent = 3.8, vegan = "no", vegetarian = "yes"),
                    Ingredient(
                        text = "stabilisers",
                        percentEstimate = 5.55,
                        subIngredients = listOf(Ingredient(text = "gellan gum carrageenan", percentEstimate = 5.55))
                    )
                ),
                ingredientsText = "1.2% fat milk (79%), Starbucks Arabica coffee coffee extract) (20.9%) milk protein powder (3.8%), stabilisers (gellan gum carrageenan)..."
            ),
            onCloseClick = {}
        )
    }
}
