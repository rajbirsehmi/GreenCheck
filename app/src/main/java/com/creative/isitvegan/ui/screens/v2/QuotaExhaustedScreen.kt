package com.creative.isitvegan.ui.screens.v2

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
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.creative.isitvegan.ui.theme.IsItVeganTheme
import com.creative.isitvegan.ui.viewmodels.AppInfoViewModel
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
            .testTag("quota_exhausted_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(40.dp)
                .testTag("quota_exhausted_content"),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f), CircleShape)
                    .testTag("quota_exhausted_icon_container"),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.HourglassTop,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("quota_exhausted_icon")
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Daily Limit Reached",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag("quota_exhausted_title")
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "You've reached your daily quota for the $featureName feature. Your limit will reset on ${formatReset(nextResetTime)}. Join our community then for a fresh exploration.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 22.sp,
                modifier = Modifier.testTag("quota_exhausted_description")
            )
            
            Spacer(modifier = Modifier.height(40.dp))
            
            Button(
                onClick = onBackToHome,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("button_quota_understood"),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "Understood",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.testTag("button_quota_understood_text")
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
