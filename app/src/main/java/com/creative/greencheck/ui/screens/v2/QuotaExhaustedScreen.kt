package com.creative.greencheck.ui.screens.v2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.creative.greencheck.testing.TestTags
import com.creative.greencheck.ui.theme.IsItVeganTheme
import com.creative.greencheck.ui.viewmodels.AppInfoViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun QuotaExhaustedScreen(
    featureName: String,
    onBackToHome: () -> Unit = {},
    viewModel: AppInfoViewModel = hiltViewModel()
) {
    val nextResetTime by viewModel.nextResetTime.collectAsStateWithLifecycle()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag(TestTags.V2.QuotaExhausted.SCREEN),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(32.dp)
                .testTag(TestTags.V2.QuotaExhausted.CONTENT),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                    .testTag(TestTags.V2.QuotaExhausted.ICON_CONTAINER),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.HourglassTop,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .size(44.dp)
                        .testTag(TestTags.V2.QuotaExhausted.ICON)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Daily Limit Reached",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.testTag(TestTags.V2.QuotaExhausted.TITLE)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "You've reached your daily quota for the $featureName feature. Your limit will reset on ${formatReset(nextResetTime)}. Join our community then for a fresh exploration.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag(TestTags.V2.QuotaExhausted.DESCRIPTION)
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = onBackToHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag(TestTags.V2.QuotaExhausted.BTN_UNDERSTOOD),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Understood",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag(TestTags.V2.QuotaExhausted.BTN_UNDERSTOOD_TEXT)
                )
            }
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun QuotaExhaustedScreenPreview() {
    IsItVeganTheme {
        QuotaExhaustedScreen("Scanner")
    }
}

private fun formatReset(timestamp: Long): String {
    if (timestamp <= 0L) return "soon"
    val sdf = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
