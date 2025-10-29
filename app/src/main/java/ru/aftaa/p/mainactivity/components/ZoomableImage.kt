package ru.aftaa.p.mainactivity.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.foundation.gestures.detectTransformGestures

@Composable
fun ZoomableImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    onZoomStateChange: (Boolean) -> Unit = {}
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    LaunchedEffect(scale) {
        onZoomStateChange(scale > 1f)
    }

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTransformGestures { centroid, pan, zoom, rotation ->
                    // Два пальца - масштабирование
                    scale = (scale * zoom).coerceIn(0.5f, 5f)

                    // Панорамирование только когда увеличены
                    if (scale > 1f) {
                        offset += pan
                    }
                }
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        // Двойной тап - увеличение/сброс
                        scale = if (scale > 1f) 1f else 3f
                        offset = Offset.Zero
                    }
                )
            }
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    translationX = offset.x
                    translationY = offset.y
                }
                .fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }

    LaunchedEffect(imageUrl) {
        scale = 1f
        offset = Offset.Zero
    }
}