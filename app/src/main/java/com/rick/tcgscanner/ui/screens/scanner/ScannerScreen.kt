package com.rick.tcgscanner.ui.screens.scanner

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import android.content.Context as AndroidContext
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.rick.tcgscanner.R
import com.rick.tcgscanner.TcgApplication
import com.rick.tcgscanner.data.model.Card
import com.rick.tcgscanner.ui.navigation.Screen
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun ScanRoute(navController: NavController) {
    val app = LocalContext.current.applicationContext as TcgApplication
    val viewModel: ScannerViewModel = viewModel(
        factory = ScannerViewModelFactory(
            app.appContainer.cardRepository,
            app.appContainer.settingsRepository
        )
    )
    ScannerScreen(
        viewModel = viewModel,
        onCardClick = { card -> navController.navigate(Screen.CardDetail.createRoute(card.id)) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScannerScreen(viewModel: ScannerViewModel, onCardClick: (Card) -> Unit) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> hasPermission = granted }
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text(stringResource(R.string.scan)) }) },
        floatingActionButton = {
            if (hasPermission && uiState is ScannerUiState.Idle) {
                ExtendedFloatingActionButton(
                    onClick = {
                        imageCapture?.let { capture ->
                            takePhoto(capture, context) { result ->
                                result.onSuccess { file -> viewModel.onPhotoCaptured(file) }
                            }
                        }
                    },
                    icon = { Icon(Icons.Default.Camera, contentDescription = null) },
                    text = { Text(stringResource(R.string.capture)) }
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                !hasPermission -> CameraPermissionRequest {
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }

                uiState is ScannerUiState.Idle -> CameraPreview(
                    modifier = Modifier.fillMaxSize(),
                    onImageCaptureReady = { imageCapture = it }
                )

                uiState is ScannerUiState.Scanning -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )

                uiState is ScannerUiState.Results -> {
                    val results = uiState as ScannerUiState.Results
                    ScanResults(
                        results = results,
                        onCardClick = onCardClick,
                        onReset = viewModel::reset,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                uiState is ScannerUiState.Error -> ErrorCard(
                    message = (uiState as ScannerUiState.Error).message,
                    onRetry = viewModel::reset,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun CameraPermissionRequest(onGrant: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(stringResource(R.string.camera_permission_required))
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onGrant) {
            Text(stringResource(R.string.grant_permission))
        }
    }
}

@Composable
private fun CameraPreview(modifier: Modifier = Modifier, onImageCaptureReady: (ImageCapture) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember {
        PreviewView(context).apply {
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }
    val imageCapture = remember { ImageCapture.Builder().build() }

    LaunchedEffect(Unit) {
        try {
            val cameraProvider = context.getCameraProvider()
            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifecycleOwner,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageCapture
            )
            onImageCaptureReady(imageCapture)
        } catch (e: Exception) {
            Log.e("CameraPreview", "Camera binding failed: ${e.message}")
        }
    }
    AndroidView(factory = { previewView }, modifier = modifier)
}

private suspend fun AndroidContext.getCameraProvider(): ProcessCameraProvider =
    suspendCoroutine { continuation ->
        val future = ProcessCameraProvider.getInstance(this)
        future.addListener({
            continuation.resume(future.get())
        }, ContextCompat.getMainExecutor(this))
    }

private fun takePhoto(
    imageCapture: ImageCapture,
    context: AndroidContext,
    onResult: (Result<File>) -> Unit
) {
    val file = File(context.cacheDir, "scan_${System.currentTimeMillis()}.jpg")
    val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()
    imageCapture.takePicture(
        outputOptions,
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                onResult(Result.success(file))
            }

            override fun onError(exception: ImageCaptureException) {
                onResult(Result.failure(exception))
            }
        }
    )
}

@Composable
private fun ScanResults(
    results: ScannerUiState.Results,
    onCardClick: (Card) -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Detected: ${results.scan.name}",
            style = MaterialTheme.typography.titleLarge
        )
        if (!results.scan.setName.isNullOrBlank()) {
            Text(
                text = "Set: ${results.scan.setName}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        if (!results.scan.condition.isNullOrBlank()) {
            Text(
                text = "Condition: ${results.scan.condition}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = if (results.cards.isEmpty()) stringResource(R.string.no_cards_found) else "Matches",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(results.cards, key = { it.id }) { card ->
                CardListItem(card = card, onClick = { onCardClick(card) })
            }
        }
        Button(onClick = onReset, modifier = Modifier.fillMaxWidth()) {
            Text("Scan another")
        }
    }
}

@Composable
private fun CardListItem(card: Card, onClick: () -> Unit) {
    OutlinedCard(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = card.smallImage,
                contentDescription = card.name,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(text = card.name, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = listOfNotNull(card.setName, card.number, card.rarity)
                        .joinToString(" · "),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun ErrorCard(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, color = MaterialTheme.colorScheme.error)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onRetry) {
            Text("Try again")
        }
    }
}
