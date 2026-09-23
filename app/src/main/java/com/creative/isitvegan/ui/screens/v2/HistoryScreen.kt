package com.creative.isitvegan.ui.screens.v2

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.creative.isitvegan.domain.model.Product
import com.creative.isitvegan.testing.TestTags
import com.creative.isitvegan.ui.components.v2.ProductItem
import com.creative.isitvegan.ui.theme.IsItVeganTheme

@Composable
fun HistoryScreen(
    products: List<Product>,
    onProductClick: (Product) -> Unit,
    onClearHistoryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .testTag(TestTags.V2.History.SCREEN)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .testTag(TestTags.V2.History.LIST),
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(products) { product ->
                ProductItem(
                    product = product,
                    onClick = onProductClick
                )
            }
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(TestTags.V2.History.CLEAR_CONTAINER),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 0.dp
        ) {
            OutlinedButton(
                onClick = onClearHistoryClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp)
                    .height(50.dp)
                    .testTag(TestTags.V2.History.BTN_CLEAR),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.2f))
            ) {
                Text(
                    text = "Clear History",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.testTag(TestTags.V2.History.BTN_CLEAR_TEXT)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun HistoryScreenPreview() {
    IsItVeganTheme {
        HistoryScreen(
            products = listOf(
                Product(barcode = "1", name = "Oat Milk", brands = "Pure Earth"),
                Product(barcode = "2", name = "Beef Burger", brands = "Meat Co", ingredientsAnalysisTags = listOf("en:non-vegan"))
            ),
            onProductClick = {},
            onClearHistoryClick = {}
        )
    }
}
