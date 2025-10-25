package ru.aftaa.p.mainactivity.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.aftaa.p.mainactivity.data.model.Photo

@Composable
fun PhotoGrid(
    photos: List<Photo>,
    isLoading: Boolean,
    error: String?,
    onRetry: () -> Unit,
    onImageClick: (Photo) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        when {
            isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Загрузка...")
                }
            }
            error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Ошибка: $error")
                        Button(onClick = onRetry) {
                            Text("Повторить")
                        }
                    }
                }
            }
            photos.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Нет фотографий")
                }
            }
            else -> {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(4.dp)
                ) {
                    items(photos) { photo ->
                        AsyncImage(
                            model = photo.thumbnailUrl,
                            contentDescription = photo.title,
                            modifier = Modifier
                                .height(120.dp)
                                .padding(4.dp)
                                .clickable { onImageClick(photo) },
                            contentScale = ContentScale.Fit // НЕ масштабировать
                        )
                    }
                }
            }
        }
    }
}