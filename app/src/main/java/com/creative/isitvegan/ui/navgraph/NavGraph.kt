package com.creative.isitvegan.ui.navgraph

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.creative.isitvegan.ui.screens.ErrorScreen
import com.creative.isitvegan.ui.screens.HomeScaffolding
import com.creative.isitvegan.ui.screens.LoadingScreen
import com.creative.isitvegan.ui.screens.ProductScreen
import com.creative.isitvegan.ui.screens.ScanItemScreen
import com.creative.isitvegan.ui.viewmodels.RecentSearchViewModel
import com.creative.isitvegan.ui.viewmodels.ScanItemViewModel

@OptIn(ExperimentalGetImage::class)
@Composable
fun NavGraph() {
    val navController = rememberNavController()
    val recentSearchViewModel = hiltViewModel<RecentSearchViewModel>()
    val scanItemViewModel = hiltViewModel<ScanItemViewModel>()
    val searches by recentSearchViewModel.products.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            scanItemViewModel.resetScanner()
            navController.navigate(Routes.SCAN)
        }
        else Toast.makeText(
            context,
            "Permission is required to scan the product",
            Toast.LENGTH_SHORT
        ).show()
    }

    val barcodeResult by scanItemViewModel.barcodeResult.collectAsStateWithLifecycle()
    val existsInDb by scanItemViewModel.existsInDb.collectAsStateWithLifecycle()

    LaunchedEffect(barcodeResult, existsInDb) {
        val code = barcodeResult
        val exists = existsInDb
        if (code != null && code.isNotEmpty() && exists != null) {
            if (exists) {
                // If it exists in DB, skip loading and go directly to product
                Log.d("TAG", "NavGraph: Existed")
                navController.navigate(Routes.getProductRoute(code)) {
                    popUpTo(Routes.SCAN) { inclusive = true }
                }
            } else {
                // If not in DB, go to loading screen to fetch from API
                Log.d("TAG", "NavGraph: New")
                navController.navigate(Routes.getLoadingRoute(code)) {
                    popUpTo(Routes.SCAN) { inclusive = true }
                }
            }
            scanItemViewModel.clearBarcodeResult()
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.HOME,
        enterTransition = { fadeIn(animationSpec = tween(400)) },
        exitTransition = { fadeOut(animationSpec = tween(400)) },
        popEnterTransition = { fadeIn(animationSpec = tween(400)) },
        popExitTransition = { fadeOut(animationSpec = tween(400)) }
    ) {
        composable(Routes.HOME) {
            HomeScaffolding(
                searches = searches,
                onScanClick = {
                    val hasPermission = ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                    if (hasPermission) {
                        scanItemViewModel.resetScanner()
                        navController.navigate(Routes.SCAN)
                    }
                    else permissionLauncher.launch(Manifest.permission.CAMERA)
                },
                onProductClick = { barcode ->
                    navController.navigate(Routes.getProductRoute(barcode))
                }
            )
        }
        composable(Routes.SCAN) {
            ScanItemScreen(
                viewModel = scanItemViewModel,
                onCloseClick = {
                    scanItemViewModel.resetScanner()
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.LOADING) { backStackEntry ->
            val barcode = backStackEntry.arguments?.getString("barcode") ?: ""
            LoadingScreen(
                barcode = barcode,
                onLoadingComplete = {
                    navController.navigate(Routes.getProductRoute(barcode)) {
                        popUpTo(Routes.LOADING) { inclusive = true }
                    }
                },
                onNotFound = {
                    Toast.makeText(context, "Product not found", Toast.LENGTH_SHORT).show()
                    navController.popBackStack(Routes.HOME, inclusive = false)
                },
                onError = {
                    navController.navigate(Routes.getErrorRoute(barcode)) {
                        popUpTo(Routes.LOADING) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.ERROR) { backStackEntry ->
            val barcode = backStackEntry.arguments?.getString("barcode") ?: ""
            ErrorScreen(
                onRetry = {
                    navController.navigate(Routes.getLoadingRoute(barcode)) {
                        popUpTo(Routes.getErrorRoute(barcode)) { inclusive = true }
                    }
                },
                onBackToHome = {
                    navController.popBackStack(Routes.HOME, inclusive = false)
                }
            )
        }
        composable(Routes.PRODUCT) { backStackEntry ->
            val barcode = backStackEntry.arguments?.getString("barcode") ?: ""
            ProductScreen(
                barcode = barcode,
                onBackClick = {
                    navController.popBackStack(Routes.HOME, inclusive = false)
                }
            )
        }
    }
}
