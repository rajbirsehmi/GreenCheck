package com.creative.greencheck.ui.screens.v2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creative.greencheck.testing.TestTags
import com.creative.greencheck.ui.theme.IsItVeganTheme

@Composable
fun EmptyHistoryScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp)
            .testTag(TestTags.V2.EmptyHistory.SCREEN),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.05f), CircleShape)
                .testTag(TestTags.V2.EmptyHistory.LOGO_CONTAINER),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.History,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .testTag(TestTags.V2.EmptyHistory.LOGO_ICON),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f)
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Your Journey Starts Here",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.testTag(TestTags.V2.EmptyHistory.TITLE)
        )
        
        Spacer(Modifier.height(12.dp))
        
        Text(
            text = "As you scan products, they will appear here for quick reference. Start exploring your favorite foods!",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 22.sp,
            modifier = Modifier.testTag(TestTags.V2.EmptyHistory.SUBTITLE)
        )
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun EmptyHistoryScreenPreview() {
    IsItVeganTheme {
        EmptyHistoryScreen()
    }
}
