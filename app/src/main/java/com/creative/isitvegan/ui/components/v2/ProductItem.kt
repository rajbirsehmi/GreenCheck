package com.creative.isitvegan.ui.components.v2

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.creative.isitvegan.domain.model.Product
import com.creative.isitvegan.testing.TestTags
import com.creative.isitvegan.ui.theme.IsItVeganTheme
import com.creative.isitvegan.ui.theme.NonVeganStatusRed
import com.creative.isitvegan.ui.theme.UncertainStatusYellow
import com.creative.isitvegan.ui.theme.VeganStatusGreen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProductItem(
    product: Product,
    modifier: Modifier = Modifier,
    onClick: (Product) -> Unit
) {
    val configuration = LocalConfiguration.current
    val dateFormat = remember(configuration) { 
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()) 
    }
    val formattedDate = dateFormat.format(Date(product.timestamp))

    val statusColor = when {
        product.isVegan -> VeganStatusGreen
        product.isNonVegan -> NonVeganStatusRed
        else -> UncertainStatusYellow
    }

    val statusText = when {
        product.isVegan -> "Vegan"
        product.isNonVegan -> "Non-Vegan"
        else -> "Uncertain"
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clickable { onClick(product) }
            .testTag(TestTags.V2.Components.ProductItem.container(product.barcode)),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val imageUrl = product.thumbUrl?.takeIf { it.isNotBlank() }
                ?: product.imageUrl?.takeIf { it.isNotBlank() }

            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f))
                    .testTag(TestTags.V2.Components.ProductItem.IMAGE),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Eco,
                    contentDescription = "Default Product Icon",
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                    modifier = Modifier.size(28.dp)
                )

                if (imageUrl != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Product Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = product.name ?: "Unnamed Product",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.testTag(TestTags.V2.Components.ProductItem.NAME)
                )
                Text(
                    text = (product.brands ?: "Unknown Brand").uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    letterSpacing = 1.sp,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.testTag(TestTags.V2.Components.ProductItem.BRAND)
                )
                Spacer(modifier = Modifier.size(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(statusColor, CircleShape)
                            .testTag(TestTags.V2.Components.ProductItem.statusDot(product.barcode))
                    )
                    Spacer(modifier = Modifier.width(6.6.dp))
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.bodySmall,
                        color = statusColor,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.testTag(TestTags.V2.Components.ProductItem.statusText(product.barcode))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "• $formattedDate",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.testTag(TestTags.V2.Components.ProductItem.timestamp(product.barcode))
                    )
                }
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ProductItemPreviewV2() {
    IsItVeganTheme {
        ProductItem(
            product = Product(
                barcode = "1234567890",
                name = "Organic Oat Milk",
                brands = "Earthly Delights",
                ingredientsAnalysisTags = listOf("en:vegan"),
                timestamp = System.currentTimeMillis()
            ),
            onClick = {}
        )
    }
}
