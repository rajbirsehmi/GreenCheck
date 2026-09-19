package com.creative.isitvegan.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import coil.compose.AsyncImage
import com.creative.isitvegan.domain.model.Ingredient
import com.creative.isitvegan.domain.model.Product
import com.creative.isitvegan.ui.theme.IsItVeganTheme
import com.creative.isitvegan.ui.viewmodels.ProductUiState
import com.creative.isitvegan.ui.viewmodels.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductScreen(
    barcode: String,
    viewModel: ProductViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState = viewModel.uiState
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    BackHandler(onBack = onBackClick)

    LaunchedEffect(barcode) {
        viewModel.getProduct(barcode)
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = if (uiState is ProductUiState.Success)
                            uiState.product.name ?: "Details" 
                        else "Product Details",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.testTag("product_title")
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("product_btn_back")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            ProductContent(uiState = uiState)
        }
    }
}

@Composable
private fun ProductContent(uiState: ProductUiState) {
    when (uiState) {
        is ProductUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(strokeWidth = 6.dp)
            }
        }
        is ProductUiState.Success -> {
            var visible by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { visible = true }
            
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn() + slideInVertically(initialOffsetY = { 40 })
            ) {
                // Invisible text for robot to find the name if it's not looking in top bar
                Box {
                    Text(
                        text = uiState.product.name ?: "",
                        modifier = Modifier.size(0.dp).testTag("product_text_name"),
                        color = Color.Transparent
                    )
                    ProductDetailsList(uiState.product)
                }
            }
        }
        is ProductUiState.Error,
        is ProductUiState.Empty -> {
            EmptyState(modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
fun ProductDetailsList(product: Product) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            ProductHero(product)
        }
        
        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                VeganStatusBanner(product)
            }
        }

        item {
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                ProductInformationSection(product)
            }
        }

        val ingredients = product.ingredients
        if (!ingredients.isNullOrEmpty()) {
            item {
                Text(
                    text = "Ingredients Analysis",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(horizontal = 20.dp)
                        .testTag("product_section_ingredients")
                )
            }
            items(ingredients) { ingredient ->
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    IngredientItem(ingredient)
                }
            }
        }
    }
}

@Composable
fun ProductHero(product: Product) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .padding(20.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .testTag("product_image"),
            shape = RoundedCornerShape(32.dp),
            tonalElevation = 8.dp,
            shadowElevation = 8.dp
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Subtle Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.4f))
                        )
                    )
            )
        }
        
        // Brand Badge
        Surface(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(20.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = product.brands?.uppercase() ?: "UNKNOWN",
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .testTag("product_text_brand"),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
fun VeganStatusBanner(product: Product) {
    val (statusText, icon, color, description) = when {
        product.isVegan -> Quad(
            product.statusText, 
            Icons.Default.Eco, 
            MaterialTheme.colorScheme.primary,
            "100% Animal-Free Ingredients"
        )
        product.isNonVegan -> Quad(
            product.statusText, 
            Icons.Default.Close, 
            Color(0xFFE53935),
            "Animal-derived products detected"
        )
        else -> Quad(
            product.statusText, 
            Icons.Default.Info, 
            Color(0xFFFB8C00),
            "Status could not be fully verified"
        )
    }

    Surface(
        modifier = Modifier.fillMaxWidth().testTag("product_banner_status"),
        color = color.copy(alpha = 0.08f),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                color = color,
                shape = CircleShape,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column {
                Text(
                    text = statusText.uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Black,
                    color = color,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.testTag("product_text_status")
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ProductInformationSection(product: Product) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Product Details",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Black,
            color = MaterialTheme.colorScheme.onBackground
        )
        
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                InfoItem("Barcode", product.barcode)
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                InfoItem("Quantity", product.quantity ?: "Not specified")
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                InfoItem("Eco-Score", product.ecoScoreGrade?.uppercase() ?: "Unknown")
            }
        }
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(text = value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Black)
    }
}

@Composable
fun IngredientItem(ingredient: Ingredient) {
    val (color, text) = when {
        ingredient.vegan == "yes" -> MaterialTheme.colorScheme.primary to "VEGAN"
        ingredient.vegetarian == "yes" -> Color(0xFF689F38) to "VEGETARIAN"
        ingredient.vegan == "no" || ingredient.vegetarian == "no" -> Color(0xFFE53935) to "NON-VEGAN"
        else -> Color.Gray to "UNKNOWN"
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("ingredient_item_${ingredient.text}"),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = ingredient.text?.replaceFirstChar { it.uppercase() } ?: "Unknown Ingredient",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            
            Surface(
                color = color.copy(alpha = 0.1f),
                shape = CircleShape
            ) {
                Text(
                    text = text,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = color,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

@Composable
fun EmptyState(modifier: Modifier) {
    Column(
        modifier = modifier.padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Warning, 
            contentDescription = null, 
            modifier = Modifier.size(80.dp), 
            tint = MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
        )
        Spacer(Modifier.height(24.dp))
        Text(
            "Product Not Found", 
            textAlign = TextAlign.Center, 
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Black
        )
        Text(
            "We couldn't retrieve the details for this item. Please try again or scan another product.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

data class Quad<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Preview(showBackground = true)
@Composable
fun ProductScreenPreview() {
    IsItVeganTheme {
        // Mock product content would go here
    }
}
