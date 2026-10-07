package com.creative.greencheck.ui.screens.v2

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.creative.greencheck.testing.TestTags
import com.creative.greencheck.ui.theme.IsItVeganTheme
import com.creative.greencheck.ui.viewmodels.ScanItemViewModel

@Composable
fun ManualEntryScreen(
    viewModel: ScanItemViewModel = hiltViewModel(),
    onSearchClick: (String) -> Unit = {}
) {
    var barcodeInput by remember { mutableStateOf("") }
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    
    val remainingManual by viewModel.remainingManual.collectAsStateWithLifecycle()

    val handleSearch = {
        val trimmed = barcodeInput.trim()
        if (trimmed.isEmpty()) {
            Toast.makeText(context, "Please enter a barcode", Toast.LENGTH_SHORT).show()
        } else if (!trimmed.all { it.isDigit() }) {
            Toast.makeText(context, "Barcode should contain digits only", Toast.LENGTH_SHORT).show()
        } else {
            keyboardController?.hide()
            onSearchClick(trimmed)
            viewModel.onManualSearchTriggered(barcodeInput)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 28.dp)
            .testTag(TestTags.V2.ManualEntry.SCREEN),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .background(MaterialTheme.colorScheme.primaryContainer, CircleShape)
                .testTag(TestTags.V2.ManualEntry.LOGO_CONTAINER),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Dialpad,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .size(36.dp)
                    .testTag(TestTags.V2.ManualEntry.LOGO_ICON)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Manual Identification",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.testTag(TestTags.V2.ManualEntry.TITLE)
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Enter the numeric UPC or EAN code found on the product packaging.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .testTag(TestTags.V2.ManualEntry.SUBTITLE)
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Material 3 Card Input Container
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .testTag(TestTags.V2.ManualEntry.INPUT_CARD),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = barcodeInput,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() || it.isWhitespace() }) {
                            val filtered = input.filter { !it.isWhitespace() }
                            if (filtered.length <= 14) {
                                barcodeInput = filtered
                            }
                        }
                    },
                    label = { Text("Product Barcode") },
                    placeholder = { Text("e.g. 012345678901") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag(TestTags.V2.ManualEntry.BARCODE_FIELD),
                    shape = MaterialTheme.shapes.medium,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                    ),
                    supportingText = {
                        Text(
                            text = "${barcodeInput.length} of 14 digits",
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag(TestTags.V2.ManualEntry.DIGIT_COUNT),
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = { handleSearch() }
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Dialpad,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.testTag(TestTags.V2.ManualEntry.FIELD_ICON)
                        )
                    }
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = { handleSearch() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag(TestTags.V2.ManualEntry.BTN_IDENTIFY),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier.testTag(TestTags.V2.ManualEntry.BTN_IDENTIFY_ICON)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Identify Product",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp,
                        modifier = Modifier.testTag(TestTags.V2.ManualEntry.BTN_IDENTIFY_TEXT)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Quota Label
        Surface(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .testTag(TestTags.V2.ManualEntry.QUOTA_CONTAINER),
            color = MaterialTheme.colorScheme.primaryContainer,
            shape = MaterialTheme.shapes.small
        ) {
            Text(
                text = "Quota: $remainingManual of 5 entries remaining",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .testTag(TestTags.V2.ManualEntry.QUOTA_TEXT)
            )
        }
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
fun ManualEntryScreenPreview() {
    IsItVeganTheme {
        ManualEntryScreen()
    }
}
