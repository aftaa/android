package ru.aftaa.p.mainactivity.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.gestures.TransformableState // ДОБАВЬТЕ ЭТОТ ИМПОРТ
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter

@Composable
fun ZoomableImage(
    imageUrl: String,
    modifier: Modifier = Modifier
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    var rotation by remember { mutableFloatStateOf(0f) }

    val context = LocalContext.current
    val displayMetrics = remember { context.resources.displayMetrics }
    val screenWidth = displayMetrics.widthPixels.toFloat()
    val screenHeight = displayMetrics.heightPixels.toFloat()

    var imageSize by remember { mutableStateOf(IntSize(0, 0)) }

    LaunchedEffect(imageSize) {
        if (imageSize.width > 0 && imageSize.height > 0) {
            val widthScale = screenWidth / imageSize.width
            val heightScale = screenHeight / imageSize.height
            scale = minOf(widthScale, heightScale).coerceAtMost(1f)
        }
    }

    val state = remember {
        TransformableState { zoomChange, offsetChange, rotationChange ->
            scale = (scale * zoomChange).coerceIn(0.5f, 5f)
            offset += offsetChange
            rotation += rotationChange
        }
    }

    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onDoubleTap = { tapOffset ->
                        if (scale > 1f) {
                            scale = 1f
                            offset = Offset.Zero
                            rotation = 0f
                        } else {
                            scale = 3f
                            val centerX = size.width / 2
                            val centerY = size.height / 2
                            val tapX = tapOffset.x - centerX
                            val tapY = tapOffset.y - centerY
                            offset = Offset(-tapX * 2, -tapY * 2)
                        }
                    }
                )
            }
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Увеличенное изображение",
            modifier = Modifier
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.rotationZ = rotation
                    translationX = offset.x * scale
                    translationY = offset.y * scale
                }
                .transformable(state) // ТЕПЕРЬ ДОЛЖНО РАБОТАТЬ
                .fillMaxSize()
                .onSizeChanged { },
            contentScale = ContentScale.Fit,
            onSuccess = { state ->
                imageSize = state.painter.intrinsicSize.let {
                    IntSize(it.width.toInt(), it.height.toInt())
                }
            }
        )
    }
}