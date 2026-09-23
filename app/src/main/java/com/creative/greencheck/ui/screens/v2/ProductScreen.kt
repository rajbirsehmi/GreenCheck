package com.creative.greencheck.ui.screens.v2

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.creative.greencheck.R
import com.creative.greencheck.domain.model.Ingredient
import com.creative.greencheck.domain.model.Product
import com.creative.greencheck.testing.TestTags
import com.creative.greencheck.ui.theme.IsItVeganTheme
import com.creative.greencheck.ui.theme.NonVeganStatusRed
import com.creative.greencheck.ui.theme.UncertainStatusYellow
import com.creative.greencheck.ui.theme.VeganStatusGreen
import com.creative.greencheck.ui.viewmodels.ProductUiState
import com.creative.greencheck.ui.viewmodels.ProductViewModel

@Composable
fun ProductScreen(
    barcode: String,
    onCloseClick: () -> Unit = {},
    viewModel: ProductViewModel = hiltViewModel()
) {
    LaunchedEffect(barcode) {
        viewModel.getProduct(barcode)
    }

    val state = viewModel.uiState

    Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
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
    }
}

@Composable
fun ProductContent(
    product: Product,
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
            Spacer(modifier = Modifier.height(24.dp))
            ProductStatusBanner(product)
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
            ProductDetailsSection(product)
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
            IngredientsAnalysisSection(product)
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
            AllIngredientsSection(product)
        }

        item {
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = onCloseClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(56.dp)
                    .testTag(TestTags.V2.Product.BTN_CLOSE),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Back to Exploration",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag(TestTags.V2.Product.BTN_CLOSE_TEXT)
                )
            }
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
                .height(300.dp)
                .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f))
                .testTag(TestTags.V2.Product.IMAGE_CONTAINER),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Eco,
                contentDescription = "Default Product Icon",
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                modifier = Modifier
                    .size(72.dp)
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
                        .testTag(TestTags.V2.Product.IMAGE),
                    contentScale = ContentScale.Crop
                )
            }
            
            // Branding Overlay
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(20.dp)
                    .testTag(TestTags.V2.Product.BRAND_OVERLAY),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.85f),
                shape = RoundedCornerShape(12.dp),
                tonalElevation = 0.dp
            ) {
                Text(
                    text = (product.brands ?: "Unknown Brand").uppercase(),
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag(TestTags.V2.Product.BRAND_NAME),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.5.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp)
                .testTag(TestTags.V2.Product.TITLE_SECTION)
        ) {
            Text(
                text = product.name ?: "Unnamed Product",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
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
    val (statusColor, statusTitle, statusDesc) = when {
        product.isVegan -> Triple(VeganStatusGreen, "VEGAN CERTIFIED", "Plant-based goodness. No animal derivatives detected.")
        product.isNonVegan -> Triple(NonVeganStatusRed, "NON-VEGAN", "Contains animal-derived ingredients.")
        else -> Triple(UncertainStatusYellow, "UNCERTAIN STATUS", "Source of some ingredients could not be verified.")
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .testTag(TestTags.V2.Product.STATUS_BANNER),
        color = statusColor.copy(alpha = 0.1f),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
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
                        .size(12.dp)
                        .background(statusColor, CircleShape)
                        .testTag(TestTags.V2.Product.STATUS_DOT)
                )
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(
                    text = statusTitle,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = statusColor,
                    modifier = Modifier.testTag(TestTags.V2.Product.STATUS_TITLE)
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = statusDesc,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp,
                    modifier = Modifier.testTag(TestTags.V2.Product.STATUS_DESCRIPTION)
                )
            }
        }
    }
}

@Composable
fun ProductDetailsSection(product: Product) {
    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .testTag(TestTags.V2.Product.DETAILS_SECTION)
    ) {
        SectionTitle("Product Details")
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .testTag(TestTags.V2.Product.DETAILS_CARD)
            ) {
                DetailRow("Quantity", product.quantity ?: "N/A")
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                DetailRow("Eco-Score", product.ecoScoreGrade?.uppercase() ?: "Unknown")
                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                DetailRow("Category", product.categories?.split(",")?.firstOrNull() ?: "General")
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(TestTags.V2.Product.detailRow(label)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.testTag(TestTags.V2.Product.detailLabel(label))
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.testTag(TestTags.V2.Product.detailValue(label))
        )
    }
}

@Composable
fun IngredientsAnalysisSection(product: Product) {
    val ingredients = product.ingredients ?: emptyList()
    val vegan = ingredients.filter { it.vegan == "yes" }.mapNotNull { it.text }
    val nonVegan = ingredients.filter { it.vegan == "no" }.mapNotNull { it.text }
    val uncertain = ingredients.filter { it.vegan != "yes" && it.vegan != "no" }.mapNotNull { it.text }

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .testTag(TestTags.V2.Product.INGREDIENTS_ANALYSIS_SECTION)
    ) {
        SectionTitle("Ingredients Analysis")
        Spacer(modifier = Modifier.height(16.dp))
        
        AnalysisCard("Vegan Friendly", VeganStatusGreen, vegan, "vegan")
        Spacer(modifier = Modifier.height(12.dp))
        AnalysisCard("Uncertain Source", UncertainStatusYellow, uncertain, "uncertain")
        Spacer(modifier = Modifier.height(12.dp))
        AnalysisCard("Non-Vegan Detected", NonVeganStatusRed, nonVegan, "non_vegan")
    }
}

@Composable
fun AnalysisCard(title: String, color: Color, items: List<String>, tagSuffix: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(TestTags.V2.Product.analysisCard(tagSuffix)),
        color = color.copy(alpha = 0.05f),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.1f))
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
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = color,
                    modifier = Modifier.testTag(TestTags.V2.Product.analysisCardTitle(tagSuffix))
                )
            }
            if (items.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Column(
                    modifier = Modifier.testTag(TestTags.V2.Product.analysisCardItems(tagSuffix)),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items.forEachIndexed { index, item ->
                        Row(
                            modifier = Modifier.testTag(TestTags.V2.Product.analysisCardItem(tagSuffix, index)),
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = "•",
                                style = MaterialTheme.typography.bodySmall,
                                color = color.copy(alpha = 0.6f),
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text(
                                text = item.replaceFirstChar { c -> c.uppercase() },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 18.sp,
                                modifier = Modifier.testTag(TestTags.V2.Product.analysisCardItemText(tagSuffix, index))
                            )
                        }
                    }
                }
            } else {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "None detected",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.testTag(TestTags.V2.Product.analysisCardEmpty(tagSuffix))
                )
            }
        }
    }
}

@Composable
fun AllIngredientsSection(product: Product) {
    val ingredients = product.ingredients?.mapNotNull { it.text } ?: emptyList()
    if (ingredients.isEmpty()) return

    Column(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .testTag(TestTags.V2.Product.ALL_INGREDIENTS_SECTION)
    ) {
        SectionTitle("Full Ingredient List")
        Spacer(modifier = Modifier.height(12.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(TestTags.V2.Product.ALL_INGREDIENTS_CARD),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .testTag(TestTags.V2.Product.ALL_INGREDIENTS_LIST),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ingredients.forEachIndexed { index, ingredient ->
                    Row(
                        modifier = Modifier.testTag(TestTags.V2.Product.allIngredientsItem(index)),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = ingredient.replaceFirstChar { c -> c.uppercase() },
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 20.sp,
                            modifier = Modifier.testTag(TestTags.V2.Product.allIngredientsItemText(index))
                        )
                    }
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
        letterSpacing = 1.5.sp,
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
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 8.dp)
                .testTag(TestTags.V2.Product.ERROR_STATE_MESSAGE)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onRetry,
            shape = RoundedCornerShape(12.dp),
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
@Preview(showBackground = true)
fun ProductScreenPreview() {
    IsItVeganTheme {
        ProductContent(
            product = Product(
                barcode = "1234567890",
                name = "Artisan Almond Cheese",
                brands = "Green Life",
                ingredients = listOf(Ingredient(text = "Almonds", vegan = "yes"), Ingredient(text = "Water", vegan = "yes"))
            ),
            onCloseClick = {}
        )
    }
}
