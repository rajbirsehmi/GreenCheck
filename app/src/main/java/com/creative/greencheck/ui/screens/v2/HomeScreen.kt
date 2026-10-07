package com.creative.greencheck.ui.screens.v2

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.creative.greencheck.R
import com.creative.greencheck.testing.TestTags
import com.creative.greencheck.ui.theme.IsItVeganTheme

@Composable
fun HomeScreen(
    onNavigateToManual: () -> Unit = {},
    onNavigateToScanner: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        // Material 3 Vibrant Botanical Hero Section
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(TestTags.V2.Home.HERO_SECTION),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.leaves),
                        contentDescription = "Botanical Logo",
                        modifier = Modifier
                            .size(44.dp)
                            .testTag(TestTags.V2.Home.LOGO),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "GreenCheck",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Conscious Scanner",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Discover Conscious Eating",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.testTag(TestTags.V2.Home.TITLE)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Simple, lightning-fast tools to verify vegan products instantly.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f),
                    modifier = Modifier.testTag(TestTags.V2.Home.SUBTITLE)
                )

                Spacer(modifier = Modifier.height(24.dp))

                ExtendedFloatingActionButton(
                    onClick = onNavigateToScanner,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.QrCodeScanner,
                            contentDescription = null
                        )
                    },
                    text = {
                        Text(
                            text = "Scan Barcode Now",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(TestTags.V2.Home.FAB_SCAN_NOW)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Quick Actions Header
        Text(
            text = "FEATURES",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.testTag(TestTags.V2.Home.QUICK_ACTIONS_TITLE)
        )
        
        Spacer(modifier = Modifier.height(12.dp))

        // Material 3 Feature List
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(TestTags.V2.Home.ACTIONS_LIST),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MinimalistActionCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(TestTags.V2.Home.CARD_SCANNER),
                title = "Barcode Scanner",
                subtitle = "Scan product barcode instantly with camera",
                icon = ImageVector.vectorResource(R.drawable.barcode_scanner),
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                onClick = onNavigateToScanner
            )
            MinimalistActionCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(TestTags.V2.Home.CARD_MANUAL),
                title = "Manual Entry",
                subtitle = "Look up product by typing UPC digits",
                icon = Icons.Default.Dialpad,
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                onClick = onNavigateToManual
            )
            MinimalistActionCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag(TestTags.V2.Home.CARD_HISTORY),
                title = "Scan History",
                subtitle = "Review previously scanned products",
                icon = Icons.Default.History,
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                onClick = onNavigateToHistory
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Material 3 Info Sections
        MinimalistInfoSection(
            title = "How it works",
            content = stringResource(R.string.how_it_works),
            modifier = Modifier.testTag(TestTags.V2.Home.SECTION_HOW_IT_WORKS)
        )

        Spacer(modifier = Modifier.height(16.dp))

        MinimalistInfoSection(
            title = "Disclaimer",
            content = stringResource(R.string.disclaimer),
            modifier = Modifier.testTag(TestTags.V2.Home.SECTION_DISCLAIMER)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun MinimalistActionCard(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    onClick: () -> Unit
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier.height(84.dp),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.elevatedCardColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                modifier = Modifier.size(28.dp),
                tint = contentColor
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = contentColor.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun MinimalistInfoSection(title: String, content: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.testTag(TestTags.V2.Home.infoSectionTitle(title))
        )
        Spacer(modifier = Modifier.height(8.dp))
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
