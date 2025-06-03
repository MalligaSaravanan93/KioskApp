package com.interview.mishi.pay.scanner

import android.Manifest
import android.annotation.SuppressLint
import androidx.camera.core.ImageAnalysis
import androidx.camera.mlkit.vision.MlKitAnalyzer
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.gson.Gson
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode

object CameraUtils {

    @SuppressLint("PermissionLaunchedDuringComposition")
    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun OpenCamera(result: (ScanResult) -> Unit) {
        val permissionState = rememberPermissionState(Manifest.permission.CAMERA)

        // Handle permission rationale
        var showRationale by remember { mutableStateOf(permissionState.status.shouldShowRationale) }

        if (showRationale && !permissionState.status.isGranted) {
            ShowRationaleDialog(
                onDismiss = {
                    showRationale = false
                    result(ScanResult.Error("Camera Permission Denied!"))
                },
                onConfirm = { permissionState.launchPermissionRequest() },
                body = "Camera permission is required to scan barcodes."
            )
        }

        // Check if permission is granted
        if (permissionState.status.isGranted) {
            CaptureContent { content ->
                try {
                    val product = Gson().fromJson(content, Product::class.java)
                    result(ScanResult.Success(product))
                } catch (e: Exception) {
                    result(ScanResult.Error("Invalid QR Code Content"))
                }
            }
        } else {
            // Request permission if not granted
            LaunchedEffect(Unit) {
                permissionState.launchPermissionRequest()
            }
        }
    }

    @Composable
    fun CaptureContent(capturedContent: (String) -> Unit) {
        val context = LocalContext.current
        val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

        // Initialize CameraController
        val cameraController = remember {
            LifecycleCameraController(context).apply {
                // Configure barcode scanning options
                val options = BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(
                        Barcode.FORMAT_QR_CODE,
                        Barcode.FORMAT_CODABAR,
                        Barcode.FORMAT_CODE_93,
                        Barcode.FORMAT_CODE_39,
                        Barcode.FORMAT_CODE_128,
                        Barcode.FORMAT_EAN_8,
                        Barcode.FORMAT_EAN_13,
                        Barcode.FORMAT_AZTEC
                    )
                    .build()

                // Initialize barcode scanner
                val barcodeScanner = BarcodeScanning.getClient(options)

                // Set up image analysis for barcode detection
                setImageAnalysisAnalyzer(
                    ContextCompat.getMainExecutor(context),
                    MlKitAnalyzer(
                        listOf(barcodeScanner),
                        ImageAnalysis.COORDINATE_SYSTEM_VIEW_REFERENCED,
                        ContextCompat.getMainExecutor(context)
                    ) { result ->
                        val barcodeResults = result?.getValue(barcodeScanner)
                        if (!barcodeResults.isNullOrEmpty()) {
                            barcodeResults.first().rawValue?.let { code ->
                                capturedContent(code)
                            }
                        }
                    }
                )
            }
        }
        // Display camera preview
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                PreviewView(ctx).apply {
                    controller = cameraController
                    cameraController.bindToLifecycle(lifecycleOwner)
                }
            }
        )
    }

    @Composable
    fun ShowRationaleDialog(
        onDismiss: () -> Unit,
        onConfirm: () -> Unit,
        title: String = "Permission Required",
        body: String = "Camera permission is required to scan barcodes."
    ) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = title) },
            text = { Text(text = body) },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        )
    }
}
