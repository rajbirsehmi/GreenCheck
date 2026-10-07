@file:OptIn(ExperimentalMaterial3Api::class)

package com.creative.greencheck.ui.screens.v2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dialpad
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.outlined.Dialpad
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.creative.greencheck.R
import com.creative.greencheck.data.local.FeatureType
import com.creative.greencheck.testing.TestTags
import com.creative.greencheck.ui.components.TopAppBarHome
import com.creative.greencheck.ui.components.v2.TransparencyInfoSheet
import com.creative.greencheck.ui.theme.IsItVeganTheme
import com.creative.greencheck.ui.viewmodels.AppInfoViewModel
import com.creative.greencheck.ui.viewmodels.RecentSearchViewModel
import com.creative.greencheck.ui.viewmodels.ScanItemViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object BottomTabs {
    const val HOME = "home"
    const val MANUAL = "manual"
    const val SCAN = "scanner"
    const val HISTORY = "history"
    const val SEARCH = "search"
    const val WELCOME = "welcome"

    // V2 Navigation routes
    const val PRODUCT = "product/{barcode}"
    const val LOADING = "loading/{barcode}"
    const val ERROR = "error"
    const val NETWORK_ERROR = "network_error"
    const val BARCODE_ERROR = "barcode_error"
    const val QUOTA = "quota/{feature}"

    fun getProductRoute(barcode: String) = "product/$barcode"
    fun getLoadingRoute(barcode: String) = "loading/$barcode"
    fun getQuotaRoute(feature: String) = "quota/$feature"
}

@androidx.annotation.OptIn(ExperimentalGetImage::class)
@Composable
fun MainScaffolding() {
    val bottomNavController = rememberNavController()
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    val context = LocalContext.current

    val scanItemViewModel = hiltViewModel<ScanItemViewModel>()
    val infoViewModel = hiltViewModel<AppInfoViewModel>()

    val hasSeenIntro by infoViewModel.hasSeenIntro.collectAsStateWithLifecycle()
    var showTransparencyInAccount by remember { mutableStateOf(false) }

    val scannerUsage by infoViewModel.scannerUsage.collectAsStateWithLifecycle()
    val manualUsage by infoViewModel.manualUsage.collectAsStateWithLifecycle()
    val ingredientUsage by infoViewModel.ingredientSearchUsage.collectAsStateWithLifecycle()
    val alternativeUsage by infoViewModel.alternativeSearchUsage.collectAsStateWithLifecycle()
    val nextResetTime by infoViewModel.nextResetTime.collectAsStateWithLifecycle()

    val navBackStackEntry by bottomNavController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val showBars = currentDestination?.route != BottomTabs.WELCOME

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            scanItemViewModel.resetScanner()
            bottomNavController.navigate(BottomTabs.SCAN) {
                popUpTo(bottomNavController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        } else Toast.makeText(
            context,
            "Permission is required to scan the product",
            Toast.LENGTH_SHORT
        ).show()
    }

    val barcodeResult by scanItemViewModel.barcodeResult.collectAsStateWithLifecycle()
    val existsInDb by scanItemViewModel.existsInDb.collectAsStateWithLifecycle()
    val quotaExhausted by scanItemViewModel.quotaExhausted.collectAsStateWithLifecycle()

    LaunchedEffect(barcodeResult, existsInDb) {
        val code = barcodeResult
        val exists = existsInDb
        if (code != null && code.isNotEmpty() && exists != null) {
            if (exists) {
                bottomNavController.navigate(BottomTabs.getProductRoute(code))
            } else {
                bottomNavController.navigate(BottomTabs.getLoadingRoute(code))
            }
            scanItemViewModel.clearBarcodeResult()
        }
    }

    LaunchedEffect(quotaExhausted) {
        quotaExhausted?.let { feature ->
            bottomNavController.navigate(BottomTabs.getQuotaRoute(feature.name))
            scanItemViewModel.resetScanner()
        }
    }

    val currentRoute = currentDestination?.route
    val isSubScreen = currentRoute in listOf(
        BottomTabs.PRODUCT,
        BottomTabs.LOADING,
        BottomTabs.BARCODE_ERROR,
        BottomTabs.NETWORK_ERROR,
        BottomTabs.ERROR,
        BottomTabs.QUOTA
    ) || currentRoute?.startsWith("product/") == true || currentRoute?.startsWith("loading/") == true || currentRoute?.startsWith(
        "quota/"
    ) == true

    val topBarTitle = when {
        currentRoute?.startsWith("product/") == true -> "Product Details"
        currentRoute?.startsWith("loading/") == true -> "Analyzing Product"
        currentRoute == BottomTabs.MANUAL -> "Manual Entry"
        currentRoute == BottomTabs.HISTORY -> "History"
        currentRoute == BottomTabs.SEARCH -> "Search Database"
        currentRoute == BottomTabs.SCAN -> "Barcode Scanner"
        else -> "GreenCheck"
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            if (showBars) {
                TopAppBarHome(
                    title = topBarTitle,
                    showBack = isSubScreen,
                    onBackClick = { bottomNavController.popBackStack() },
                    onAccountClick = { showBottomSheet = true }
                )
            }
        },
        bottomBar = {
            if (showBars) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    tonalElevation = 3.dp,
                    modifier = Modifier.testTag(TestTags.V2.Scaffolding.BOTTOM_NAV_BAR)
                ) {
                    val navItems = listOf(
                        Tuple4(BottomTabs.HOME, Icons.Filled.Home, Icons.Outlined.Home, "Home"),
                        Tuple4(
                            BottomTabs.MANUAL,
                            Icons.Filled.Dialpad,
                            Icons.Outlined.Dialpad,
                            "Manual"
                        ),
                        Tuple4(
                            BottomTabs.SCAN,
                            ImageVector.vectorResource(R.drawable.barcode_scanner),
                            ImageVector.vectorResource(R.drawable.barcode_scanner),
                            "Scan"
                        ),
                        Tuple4(
                            BottomTabs.HISTORY,
                            Icons.Filled.History,
                            Icons.Outlined.History,
                            "History"
                        ),
//                        Tuple4(BottomTabs.SEARCH, Icons.Filled.Search, Icons.Outlined.Search, "Search")
                    )

                    val navigateToTab: (String) -> Unit = { route ->
                        if (route == BottomTabs.SCAN) {
                            val hasPermission = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.CAMERA
                            ) == PackageManager.PERMISSION_GRANTED

                            if (hasPermission) {
                                scanItemViewModel.resetScanner()
                                bottomNavController.navigate(route) {
                                    popUpTo(bottomNavController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            } else {
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            }
                        } else {
                            bottomNavController.navigate(route) {
                                popUpTo(bottomNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }

                    val isMainTab = currentDestination?.route in listOf(
                        BottomTabs.HOME,
                        BottomTabs.MANUAL,
                        BottomTabs.SCAN,
                        BottomTabs.HISTORY,
                        BottomTabs.SEARCH
                    )
                    BackHandler(enabled = isMainTab) {
                        (context as? android.app.Activity)?.finish()
                    }

                    navItems.forEach { (route, selectedIcon, unselectedIcon, label) ->
                        val isSelected = currentDestination?.route == route
                        NavigationBarItem(
                            selected = isSelected,
                            modifier = Modifier.testTag(TestTags.V2.Scaffolding.navItem(route)),
                            onClick = { navigateToTab(route) },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) selectedIcon else unselectedIcon,
                                    contentDescription = label,
                                    modifier = Modifier.padding(bottom = 2.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
            startDestination = if (hasSeenIntro) BottomTabs.HOME else BottomTabs.WELCOME,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(BottomTabs.WELCOME) {
                WelcomeScreen(
                    onGetStartedClick = {
                        infoViewModel.setHasSeenIntro(true)
                        bottomNavController.navigate(BottomTabs.HOME) {
                            popUpTo(BottomTabs.WELCOME) { inclusive = true }
                        }
                    }
                )
            }
            composable(BottomTabs.HOME) {
                val navigateToTab: (String) -> Unit = { route ->
                    if (route == BottomTabs.SCAN) {
                        val hasPermission = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED

                        if (hasPermission) {
                            scanItemViewModel.resetScanner()
                            bottomNavController.navigate(route) {
                                popUpTo(bottomNavController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        } else {
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    } else {
                        bottomNavController.navigate(route) {
                            popUpTo(bottomNavController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }

                HomeScreen(
                    onNavigateToManual = { navigateToTab(BottomTabs.MANUAL) },
                    onNavigateToScanner = { navigateToTab(BottomTabs.SCAN) },
                    onNavigateToHistory = { navigateToTab(BottomTabs.HISTORY) },
                    onNavigateToSearch = { navigateToTab(BottomTabs.SEARCH) }
                )
            }
            composable(BottomTabs.MANUAL) {
                ManualEntryScreen(
                    onSearchClick = { barcode ->
                        bottomNavController.navigate(BottomTabs.getLoadingRoute(barcode))
                    }
                )
            }
            composable(BottomTabs.SCAN) {
                ScannerScreen(viewModel = scanItemViewModel)
            }
            composable(BottomTabs.HISTORY) {
                val viewModel: RecentSearchViewModel = hiltViewModel()
                val products by viewModel.products.collectAsStateWithLifecycle()

                if (products.isNotEmpty()) {
                    HistoryScreen(
                        products = products,
                        onProductClick = { product ->
                            bottomNavController.navigate(BottomTabs.getProductRoute(product.barcode))
                        },
                        onClearHistoryClick = {
                            viewModel.clearHistory()
                        }
                    )
                } else {
                    EmptyHistoryScreen()
                }
            }
//            composable(BottomTabs.SEARCH) {
//                SearchProductOrIngredientScreen(
//                    onProductClick = { product ->
//                        bottomNavController.navigate(BottomTabs.getProductRoute(product.barcode))
//                    },
//                    onQuotaExhausted = { featureName ->
//                        bottomNavController.navigate(BottomTabs.getQuotaRoute(featureName))
//                    }
//                )
//            }
            composable(BottomTabs.PRODUCT) { backStackEntry ->
                val barcode = backStackEntry.arguments?.getString("barcode") ?: ""
                ProductScreen(
                    barcode = barcode,
                    onCloseClick = {
                        bottomNavController.popBackStack(BottomTabs.HOME, false)
                    },
                    onProductClick = { product ->
                        bottomNavController.navigate(BottomTabs.getProductRoute(product.barcode))
                    },
                    onQuotaExhausted = { featureName ->
                        bottomNavController.navigate(BottomTabs.getQuotaRoute(featureName))
                    }
                )
            }
            composable(BottomTabs.LOADING) { backStackEntry ->
                val barcode = backStackEntry.arguments?.getString("barcode") ?: ""
                LoadingProductScreen(
                    barcode = barcode,
                    onLoadingComplete = {
                        bottomNavController.navigate(BottomTabs.getProductRoute(barcode)) {
                            popUpTo(BottomTabs.LOADING) { inclusive = true }
                        }
                    },
                    onBarcodeError = {
                        bottomNavController.navigate(BottomTabs.BARCODE_ERROR) {
                            popUpTo(BottomTabs.LOADING) { inclusive = true }
                        }
                    },
                    onNetworkError = {
                        bottomNavController.navigate(BottomTabs.NETWORK_ERROR) {
                            popUpTo(BottomTabs.LOADING) { inclusive = true }
                        }
                    }
                )
            }
            composable(BottomTabs.BARCODE_ERROR) {
                BarcodeErrorScreen(
                    onBackToHome = {
                        bottomNavController.navigate(BottomTabs.HOME) {
                            popUpTo(BottomTabs.HOME) { inclusive = true }
                        }
                    }
                )
            }
            composable(BottomTabs.NETWORK_ERROR) {
                NetworkErrorScreen(
                    onBackToHome = {
                        bottomNavController.navigate(BottomTabs.HOME) {
                            popUpTo(BottomTabs.HOME) { inclusive = true }
                        }
                    }
                )
            }
            composable(BottomTabs.ERROR) {
                NetworkErrorScreen(
                    onBackToHome = {
                        bottomNavController.navigate(BottomTabs.HOME) {
                            popUpTo(BottomTabs.HOME) { inclusive = true }
                        }
                    }
                )
            }
            composable(BottomTabs.QUOTA) { backStackEntry ->
                val feature = backStackEntry.arguments?.getString("feature") ?: "Feature"
                QuotaExhaustedScreen(
                    featureName = feature.replace("_", " "),
                    onBackToHome = {
                        bottomNavController.navigate(BottomTabs.HOME) {
                            popUpTo(BottomTabs.HOME) { inclusive = true }
                        }
                    }
                )
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxHeight()
                    .testTag(TestTags.V2.Scaffolding.SHEET_APP_USAGE)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                ) {
                    Text(
                        text = "App Usage",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(bottom = 16.dp)
                            .testTag(TestTags.V2.Scaffolding.USAGE_SHEET_TITLE)
                    )
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                                alpha = 0.3f
                            )
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.testTag(TestTags.V2.Scaffolding.USAGE_CARD)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            UsageRow("Barcode Scans", scannerUsage, FeatureType.SCANNER.limit)
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 12.dp),
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            )
                            UsageRow("Manual Entries", manualUsage, FeatureType.MANUAL_ENTRY.limit)
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 12.dp),
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            )
                            UsageRow(
                                "Ingredient Searches",
                                ingredientUsage,
                                FeatureType.SEARCH_INGREDIENT.limit
                            )
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 12.dp),
                                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            )
                            UsageRow(
                                "Alternative Searches",
                                alternativeUsage,
                                FeatureType.ALTERNATIVE_SEARCH.limit
                            )

                            if (nextResetTime > 0) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 12.dp),
                                    color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                                )
                                Text(
                                    text = "Resets on: ${formatTimestamp(nextResetTime)}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .testTag(TestTags.V2.Scaffolding.USAGE_RESET_TIME)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .testTag(TestTags.V2.Scaffolding.BTN_OPEN_FOOD_FACTS)
                            .clickable {
                                val intent = Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://world.openfoodfacts.org/")
                                )
                                context.startActivity(intent)
                            },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Data by Open Food Facts",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f),
                            textDecoration = TextDecoration.Underline,
                            modifier = Modifier.testTag(TestTags.V2.Scaffolding.LINK_OFF_TEXT)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "(ODbL)",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.testTag(TestTags.V2.Scaffolding.LINK_ODBL_TEXT)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = { showTransparencyInAccount = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag(TestTags.V2.Scaffolding.BTN_DATA_TRANSPARENCY),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            modifier = Modifier.testTag(TestTags.V2.Scaffolding.BTN_DATA_TRANSPARENCY_ICON)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Data Transparency")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = { showBottomSheet = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag(TestTags.V2.Scaffolding.BTN_CLOSE_USAGE_SHEET),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Close")
                    }
                }
            }
        }

        if (showTransparencyInAccount) {
            ModalBottomSheet(
                onDismissRequest = { showTransparencyInAccount = false },
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.testTag(TestTags.V2.Scaffolding.SHEET_TRANSPARENCY_IN_ACCOUNT)
            ) {
                TransparencyInfoSheet()
            }
        }
    }
}

@Composable
fun UsageRow(label: String, remaining: Int, limit: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(TestTags.V2.Scaffolding.usageRow(label)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.testTag(TestTags.V2.Scaffolding.usageLabel(label))
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "$remaining / $limit left",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = if (remaining == 0) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
            modifier = Modifier.testTag(TestTags.V2.Scaffolding.usageValue(label))
        )
    }
}

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

private data class Tuple4<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
@Preview
fun MainScaffoldingPreview() {
    IsItVeganTheme {
        MainScaffolding()
    }
}
