package com.creative.isitvegan.ui.screens.v2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.creative.isitvegan.testing.TestTags
import com.creative.isitvegan.ui.theme.IsItVeganTheme
import com.creative.isitvegan.ui.viewmodels.LoadingUiState
import com.creative.isitvegan.ui.viewmodels.LoadingViewModel

@Composable
fun LoadingProductScreen(
    barcode: String,
    viewModel: LoadingViewModel = hiltViewModel(),
    onLoadingComplete: () -> Unit = {},
    onError: () -> Unit = {}
) {
    val uiState = viewModel.uiState

    LaunchedEffect(barcode) {
        viewModel.getProduct(barcode)
    }

    LaunchedEffect(uiState) {
        when (uiState) {
            is LoadingUiState.Success -> onLoadingComplete()
            is LoadingUiState.Error -> onError()
            else -> {}
        }
    }

    LoadingProductContent()
}

@Composable
fun LoadingProductContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag(TestTags.V2.LoadingProduct.SCREEN),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.testTag(TestTags.V2.LoadingProduct.CONTENT)
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(56.dp)
                    .testTag(TestTags.V2.LoadingProduct.INDICATOR),
                strokeWidth = 4.dp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Analyzing Product",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.testTag(TestTags.V2.LoadingProduct.TITLE)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Sourcing ingredients and verifying status...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 0.5.sp,
                modifier = Modifier.testTag(TestTags.V2.LoadingProduct.SUBTITLE)
            )
        }
    }
}

@Composable
@Preview(showBackground = true)
fun LoadingProductScreenPreview() {
    IsItVeganTheme {
        LoadingProductContent()
    }
}
