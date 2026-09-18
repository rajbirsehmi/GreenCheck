package com.creative.isitvegan.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.creative.isitvegan.ui.theme.IsItVeganTheme
import com.creative.isitvegan.ui.viewmodels.LoadingViewModel
import kotlinx.coroutines.delay

@Composable
fun LoadingScreen(
    barcode: String,
    viewModel: LoadingViewModel = hiltViewModel(),
    onLoadingComplete: () -> Unit,
    onNotFound: () -> Unit,
    onError: () -> Unit
) {
    val uiState = viewModel.uiState

    LaunchedEffect(barcode) {
        viewModel.getProduct(barcode)
    }

    var navigated by remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        if (navigated) return@LaunchedEffect

        when (uiState) {
            is com.creative.isitvegan.ui.viewmodels.LoadingUiState.Success -> {
                navigated = true
                onLoadingComplete()
            }
            is com.creative.isitvegan.ui.viewmodels.LoadingUiState.Error -> {
                navigated = true
                if (uiState.message.contains("not found", ignoreCase = true)) {
                    onNotFound()
                } else {
                    onError()
                }
            }
            else -> {}
        }
    }

    LoadingContent()
}

@Composable
private fun LoadingContent() {
    val loadingMessages = listOf(
        "Analyzing ingredients...",
        "Checking vegan status...",
        "Decoding labels...",
        "Almost there..."
    )
    var currentMessageIndex by remember { mutableIntStateOf(0) }
    
    LaunchedEffect(Unit) {
        while(true) {
            delay(2000)
            currentMessageIndex = (currentMessageIndex + 1) % loadingMessages.size
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(120.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .scale(scale)
                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f), CircleShape)
                )
                Icon(
                    imageVector = Icons.Default.Eco,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("loading_icon_eco")
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            CircularProgressIndicator(
                modifier = Modifier
                    .size(32.dp)
                    .testTag("loading_progress_bar"),
                strokeWidth = 3.dp,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = loadingMessages[currentMessageIndex],
                modifier = Modifier.testTag("loading_text_message"),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
@Preview
fun LoadingScreenPreview() {
    IsItVeganTheme {
        LoadingContent()
    }
}
