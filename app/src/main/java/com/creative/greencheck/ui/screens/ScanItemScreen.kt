package com.creative.greencheck.ui.screens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.creative.greencheck.ui.viewmodels.ScanItemViewModel
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

import androidx.lifecycle.compose.collectAsStateWithLifecycle

@ExperimentalGetImage
@Composable
fun ScanItemScreen(
    viewModel: ScanItemViewModel,
    onCloseClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val haptic = LocalHapticFeedback.current

    BackHandler(onBack = onCloseClick)

    val scanner = remember { BarcodeScanning.getClient() }
    DisposableEffect(Unit) {
        onDispose {
            scanner.close()
        }
    }
    val isScanComplete by viewModel.isScanComplete.collectAsStateWithLifecycle()

    // Initialize CameraController
    val cameraController = remember {
        LifecycleCameraController(context).apply {
            // Enable Image Analysis for barcode scanning logic
            setEnabledUseCases(CameraController.IMAGE_ANALYSIS)

            setImageAnalysisAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                // STOP processing if scan is already complete
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
                                Log.d("TAG", "Barcode Read: $code")
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
            .background(Color.Black)
    ) {
        // Camera Preview Layer
        AndroidView(
            modifier = Modifier.fillMaxSize(),
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

        // The Viewfinder Overlay
        ViewfinderOverlay()

        // Close Button
        IconButton(
            onClick = onCloseClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 48.dp, start = 16.dp)
                .clip(CircleShape)
                .background(Color.Black.copy(alpha = 0.3f))
                .testTag("btn_scan_close")
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close Camera",
                tint = Color.White
            )
        }

        // Instructions Text
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp)
                .testTag("text_scan_instruction"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isScanComplete) "Barcode Detected!" else "Scan Barcode",
                style = MaterialTheme.typography.headlineSmall,
                color = if (isScanComplete) MaterialTheme.colorScheme.primary else Color.White,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = if (isScanComplete) "Processing..." else "Place the barcode inside the frame to scan",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun ViewfinderOverlay() {
    val primaryColor = MaterialTheme.colorScheme.primary
    Canvas(modifier = Modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        val rectSize = width * 0.75f
        val left = (width - rectSize) / 2
        val top = (height - rectSize) / 2.5f
        val right = left + rectSize
        val bottom = top + rectSize
        val cornerRadius = 24.dp.toPx()

        val backgroundPath = Path().apply {
            addRect(Rect(0f, 0f, width, height))
            addRoundRect(
                RoundRect(
                    rect = Rect(left, top, right, bottom),
                    cornerRadius = CornerRadius(cornerRadius)
                )
            )
            fillType = PathFillType.EvenOdd
        }
        drawPath(backgroundPath, Color.Black.copy(alpha = 0.6f))

        val cornerLength = 40.dp.toPx()
        val strokeWidth = 4.dp.toPx()
        val color = primaryColor

        // Top Left
        drawPath(
            path = Path().apply {
                moveTo(left, top + cornerLength); lineTo(left, top + cornerRadius)
                arcTo(
                    Rect(left, top, left + cornerRadius * 2, top + cornerRadius * 2),
                    180f,
                    90f,
                    false
                )
                lineTo(left + cornerLength, top)
            },
            color = color, style = Stroke(strokeWidth)
        )
        // Top Right
        drawPath(
            path = Path().apply {
                moveTo(right - cornerLength, top); lineTo(right - cornerRadius, top)
                arcTo(
                    Rect(right - cornerRadius * 2, top, right, top + cornerRadius * 2),
                    270f,
                    90f,
                    false
                )
                lineTo(right, top + cornerLength)
            },
            color = color, style = Stroke(strokeWidth)
        )
        // Bottom Left
        drawPath(
            path = Path().apply {
                moveTo(left, bottom - cornerLength); lineTo(left, bottom - cornerRadius)
                arcTo(
                    Rect(left, bottom - cornerRadius * 2, left + cornerRadius * 2, bottom),
                    180f,
                    -90f,
                    false
                )
                lineTo(left + cornerLength, bottom)
            },
            color = color, style = Stroke(strokeWidth)
        )
        // Bottom Right
        drawPath(
            path = Path().apply {
                moveTo(right - cornerLength, bottom); lineTo(right - cornerRadius, bottom)
                arcTo(
                    Rect(right - cornerRadius * 2, bottom - cornerRadius * 2, right, bottom),
                    90f,
                    -90f,
                    false
                )
                lineTo(right, bottom - cornerLength)
            },
            color = color, style = Stroke(strokeWidth)
        )
    }
}
