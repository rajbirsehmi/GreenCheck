package com.creative.isitvegan.ui.screens.v2

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creative.isitvegan.R
import com.creative.isitvegan.testing.TestTags
import com.creative.isitvegan.ui.theme.IsItVeganTheme

@Composable
fun HomeScreen(
    onNavigateToManual: () -> Unit = {},
    onNavigateToScanner: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {},
    onNavigateToSearch: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        // Hero Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(TestTags.V2.Home.HERO_SECTION),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.leaves),
                contentDescription = "Botanical Logo",
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 16.dp)
                    .testTag(TestTags.V2.Home.LOGO),
                contentScale = ContentScale.Fit
            )
            Text(
                text = "Discover Conscious Eating",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.testTag(TestTags.V2.Home.TITLE)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Simple tools to identify vegan products instantly.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .testTag(TestTags.V2.Home.SUBTITLE)
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Quick Actions
        Text(
            text = "QUICK ACTIONS",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.testTag(TestTags.V2.Home.QUICK_ACTIONS_TITLE)
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(TestTags.V2.Home.ACTIONS_ROW_1),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HomeActionCard(
                modifier = Modifier
                    .weight(1f)
                    .testTag(TestTags.V2.Home.CARD_SCANNER),
                title = "Scanner",
                subtitle = "Scan Barcode",
                icon = ImageVector.vectorResource(R.drawable.barcode_scanner),
                onClick = onNavigateToScanner
            )
            HomeActionCard(
                modifier = Modifier
                    .weight(1f)
                    .testTag(TestTags.V2.Home.CARD_SEARCH),
                title = "Search",
                subtitle = "Browse Database",
                icon = Icons.Default.Search,
                onClick = onNavigateToSearch
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(TestTags.V2.Home.ACTIONS_ROW_2),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HomeActionCard(
                modifier = Modifier
                    .weight(1f)
                    .testTag(TestTags.V2.Home.CARD_MANUAL),
                title = "Manual",
                subtitle = "Type UPC Code",
                icon = Icons.Default.Dialpad,
                onClick = onNavigateToManual
            )
            HomeActionCard(
                modifier = Modifier
                    .weight(1f)
                    .testTag(TestTags.V2.Home.CARD_HISTORY),
                title = "History",
                subtitle = "Recent Finds",
                icon = Icons.Default.History,
                onClick = onNavigateToHistory
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Info Sections
        InfoSection(
            title = "How it works",
            content = stringResource(R.string.how_it_works),
            modifier = Modifier.testTag(TestTags.V2.Home.SECTION_HOW_IT_WORKS)
        )

        Spacer(modifier = Modifier.height(24.dp))

        InfoSection(
            title = "Disclaimer",
            content = stringResource(R.string.disclaimer),
            modifier = Modifier.testTag(TestTags.V2.Home.SECTION_DISCLAIMER)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun HomeActionCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(140.dp),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun InfoSection(title: String, content: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.5.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.testTag(TestTags.V2.Home.infoSectionTitle(title))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
        ) {
            Text(
                text = content,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 18.sp,
                modifier = Modifier
                    .padding(16.dp)
                    .testTag(TestTags.V2.Home.infoSectionContent(title))
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun HomeScreenPreview() {
    IsItVeganTheme {
        HomeScreen()
    }
}
