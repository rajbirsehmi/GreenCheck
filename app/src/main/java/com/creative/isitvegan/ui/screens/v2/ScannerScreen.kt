package com.creative.isitvegan.ui.screens.v2

import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.NotificationsOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.creative.isitvegan.R
import com.creative.isitvegan.ui.theme.IsItVeganTheme
import com.creative.isitvegan.ui.viewmodels.ScanItemViewModel
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

@OptIn(ExperimentalGetImage::class)
@Composable
fun ScannerScreen(
    viewModel: ScanItemViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val haptic = LocalHapticFeedback.current

    var isSoundEnabled by remember { mutableStateOf(true) }
    val isScanComplete by viewModel.isScanComplete.collectAsStateWithLifecycle()
    val remainingScans by viewModel.remainingScans.collectAsStateWithLifecycle()

    val scanner = remember { BarcodeScanning.getClient() }
    DisposableEffect(Unit) {
        onDispose {
            scanner.close()
        }
    }

    // Initialize CameraController
    val cameraController = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(CameraController.IMAGE_ANALYSIS)
            setImageAnalysisAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                if (viewModel.isScanComplete.value) {
                    imageProxy.close()
                    return@setImageAnalysisAnalyzer
                }

                val mediaImage = imageProxy.image
                if (mediaImage != null) {
                    val image =
                        InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                    scanner.process(image)
                        .addOnSuccessListener { barcodes ->
                            if (barcodes.isNotEmpty()) {
                                val code = barcodes[0].rawValue ?: ""
                                Log.d("ScannerScreen", "Barcode Read: $code")
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                viewModel.onBarcodeDetected(code)
                            }
                        }
                        .addOnCompleteListener {
                            imageProxy.close()
                        }
                } else imageProxy.close()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("scanner_screen_container")
    ) {
        // Camera Preview Background
        AndroidView(
            modifier = Modifier
                .fillMaxSize()
                .testTag("camera_preview"),
            factory = { context ->
                PreviewView(context).apply {
                    this.controller = cameraController
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    cameraController.bindToLifecycle(lifecycleOwner)
                }
            },
            onRelease = {
                cameraController.unbind()
            }
        )

        // Minimalist Overlays
        Column(modifier = Modifier.fillMaxSize()) {
            // Top Instructions
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .testTag("scanner_instructions_overlay"),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                shape = RoundedCornerShape(20.dp),
                tonalElevation = 0.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.barcode_scanner),
                        contentDescription = null,
                        modifier = Modifier
                            .size(24.dp)
                            .testTag("scanner_icon"),
                        tint = if (isScanComplete) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = if (isScanComplete) "Detected" else "Align Barcode",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = if (isScanComplete) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier
                            .weight(1f)
                            .testTag("scanner_status_text")
                    )
                    IconButton(
                        onClick = { isSoundEnabled = !isSoundEnabled },
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("button_toggle_sound")
                    ) {
                        Icon(
                            imageVector = if (isSoundEnabled) Icons.Outlined.Notifications else Icons.Outlined.NotificationsOff,
                            contentDescription = "Toggle Sound",
                            modifier = Modifier
                                .size(20.dp)
                                .testTag("button_toggle_sound_icon")
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Bottom Quota Info
            Surface(
                modifier = Modifier
                    .padding(24.dp)
                    .align(Alignment.CenterHorizontally)
                    .testTag("scanner_quota_overlay"),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Free Plan: $remainingScans of 10 Scans today",
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .testTag("scanner_quota_text")
                )
            }
        }

        // Viewfinder (Transparent center)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .testTag("scanner_viewfinder_container"),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .size(width = 260.dp, height = 180.dp)
                    .testTag("scanner_viewfinder_window"),
                color = Color.Transparent,
                border = BorderStroke(
                    width = 1.dp,
                    color = if (isScanComplete) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                // corner accents could be added here
            }
        }
    }
}

@Composable
@Preview
fun PreviewScannerScreen() {
    IsItVeganTheme {
        ScannerScreen()
    }
}
