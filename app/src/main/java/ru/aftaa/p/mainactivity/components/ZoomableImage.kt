package ru.aftaa.p.mainactivity.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import coil.compose.AsyncImage

@Composable
fun ZoomableImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    onZoomStateChange: (Boolean) -> Unit = {}
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var imageSize by remember { mutableStateOf<androidx.compose.ui.geometry.Size?>(null) }

    LaunchedEffect(scale) {
        onZoomStateChange(scale > 1f)
    }

    Box(
        modifier = modifier
            .pointerInput(scale) {
                if (scale > 1f) {
                    detectTransformGestures { centroid, pan, zoom, rotation ->
                        // УСИЛЕННОЕ масштабирование
                        val boostedZoom = when {
                            zoom > 1f -> 1f + (zoom - 1f) * 3f  // Усиливаем увеличение
                            zoom < 1f -> 1f - (1f - zoom) * 2f  // Усиливаем уменьшение
                            else -> zoom
                        }

                        scale = (scale * boostedZoom).coerceIn(1f, 10f)

                        // Панорамирование
                        val newOffset = offset + pan
                        imageSize?.let { size ->
                            val maxOffsetX = (scale - 1f) * size.width / 2f
                            val maxOffsetY = (scale - 1f) * size.height / 2f

                            offset = Offset(
                                x = newOffset.x.coerceIn(-maxOffsetX, maxOffsetX),
                                y = newOffset.y.coerceIn(-maxOffsetY, maxOffsetY)
                            )
                        } ?: run {
                            offset = newOffset
                        }
                    }
                }
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        if (scale > 1f) {
                            scale = 1f
                            offset = Offset.Zero
                        } else {
                            scale = 2f
                        }
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
                .fillMaxSize()
                .onGloballyPositioned { layoutCoordinates ->
                    imageSize = androidx.compose.ui.geometry.Size(
                        layoutCoordinates.size.width.toFloat(),
                        layoutCoordinates.size.height.toFloat()
                    )
                },
            contentScale = ContentScale.Fit
        )
    }
}