@file:OptIn(ExperimentalMaterial3Api::class)

package com.creative.isitvegan.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@Composable
fun TopAppBarHome() {
    CenterAlignedTopAppBar(
        title = {
            Text(
                "GreenCheck",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Black,
                    color = MaterialTheme.colorScheme.onBackground,
                    letterSpacing = (-0.5).sp
                ),
                modifier = Modifier.testTag("top_bar_title")
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        )
    )
}

@Composable
@Preview
fun TopAppBarHomePreview() {
    TopAppBarHome()
}
