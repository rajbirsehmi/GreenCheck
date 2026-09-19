package com.creative.isitvegan.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.creative.isitvegan.domain.model.Product
import com.creative.isitvegan.ui.components.TopAppBarHome
import com.creative.isitvegan.ui.theme.IsItVeganTheme
import com.creative.isitvegan.ui.viewmodels.RecentSearchViewModel

@Composable
fun HomeScaffolding(
    searches: List<Product>,
    onScanClick: () -> Unit,
    onProductClick: (String) -> Unit,
    viewModel: RecentSearchViewModel = hiltViewModel()
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBarHome()
        },
        floatingActionButton = {
            if (searches.isNotEmpty()) {
                FloatingActionButton(
                    modifier = Modifier.testTag("btn_scan"),
                    onClick = onScanClick,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    Icon(
                        imageVector = Icons.Rounded.QrCodeScanner,
                        contentDescription = "Scan"
                    )
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (searches.isEmpty()) {
                EmptyScreen(onScanClick = onScanClick)
            } else {
                RecentScreen(
                    products = searches,
                    onProductClick = onProductClick,
                    onDeleteProduct = { viewModel.deleteProduct(it) }
                )
            }
        }
    }
}

@Composable
@Preview
fun HomeScaffoldingPreview() {
    IsItVeganTheme {
        HomeScaffolding(
            searches = emptyList(),
            onScanClick = {},
            onProductClick = {}
        )
    }
}
