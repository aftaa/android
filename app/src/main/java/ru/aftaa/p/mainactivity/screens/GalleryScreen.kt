package ru.aftaa.p.mainactivity.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.aftaa.p.mainactivity.components.AlbumGrid
import ru.aftaa.p.mainactivity.components.PhotoGrid
import ru.aftaa.p.mainactivity.data.model.Photo
import ru.aftaa.p.mainactivity.viewmodel.GalleryViewModel
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.delay

@Composable
fun GalleryScreen(
    viewModel: GalleryViewModel = viewModel(),
    onImageClick: (Photo) -> Unit
) {
    val currentAlbums = viewModel.currentAlbums.value
    val currentPhotos = viewModel.currentPhotos.value
    val isLoading = viewModel.isLoading.value
    val error = viewModel.error.value
    val currentAlbumTitle = viewModel.currentAlbumTitle.value
    val canGoBack = viewModel.canGoBack.value
    val currentPhotoIndex = viewModel.currentPhotoIndex.value // ДОБАВЛЯЕМ

    // Добавляем состояние скролла
    val listState = rememberLazyGridState()

    // Восстанавливаем позицию при возврате к фото
    LaunchedEffect(currentPhotoIndex, currentPhotos.isNotEmpty()) {
        if (currentPhotoIndex > 0 && currentPhotos.isNotEmpty()) {
            delay(100)
            listState.animateScrollToItem(currentPhotoIndex)
        }
    }

    // Перехватываем системную кнопку "Назад"
    BackHandler(enabled = canGoBack) {
        viewModel.goBack()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(currentAlbumTitle) },
                navigationIcon = {
                    if (canGoBack) {
                        IconButton(onClick = { viewModel.goBack() }) {
                            Icon(Icons.Default.ArrowBack, "Назад")
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            if (currentPhotos.isNotEmpty()) {
                PhotoGrid(
                    photos = currentPhotos,
                    isLoading = isLoading,
                    error = error,
                    onRetry = { viewModel.retry() },
                    onImageClick = { photo ->
                        // Сохраняем индекс перед переходом
                        val index = currentPhotos.indexOfFirst { it.id == photo.id }
                        if (index >= 0) {
                            viewModel.setCurrentPhotoIndex(index)
                        }
                        onImageClick(photo)
                    },
                    listState = listState // ПЕРЕДАЕМ состояние скролла
                )
            } else {
                AlbumGrid(
                    albums = currentAlbums,
                    onAlbumClick = { album ->
                        viewModel.navigateToAlbum(album)
                    }
                )
            }
        }
    }
}