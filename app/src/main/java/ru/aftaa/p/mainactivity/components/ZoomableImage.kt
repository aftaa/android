package ru.aftaa.p.mainactivity.components

import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.gestures.TransformableState
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.detectTapGestures

@Composable
fun ZoomableImage(
    imageUrl: String,
    modifier: Modifier = Modifier,
    onZoomStateChange: (Boolean) -> Unit = {}
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    // Отслеживаем изменения зума
    LaunchedEffect(scale) {
        onZoomStateChange(scale > 1f)
    }

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = {
                        // Увеличиваем до 3x
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

        LaunchedEffect(imageUrl) {
            scale = 1f
            offset = Offset.Zero
        }
    }
}