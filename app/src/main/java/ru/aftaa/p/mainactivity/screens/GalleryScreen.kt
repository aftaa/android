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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
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
    val currentPhotoIndex = viewModel.currentPhotoIndex.value
    val currentAlbumScrollIndex = viewModel.currentAlbumScrollIndex.value

    // ДОБАВЛЯЕМ: состояние скролла для альбомов с восстановлением из ViewModel
    val albumsListState = rememberLazyGridState(
        initialFirstVisibleItemIndex = currentAlbumScrollIndex
    )
    val photosListState = rememberLazyGridState()

    // Восстанавливаем позицию при возврате к фото
    LaunchedEffect(currentPhotoIndex, currentPhotos.isNotEmpty()) {
        if (currentPhotoIndex > 0 && currentPhotos.isNotEmpty()) {
            delay(100)
            photosListState.animateScrollToItem(currentPhotoIndex)
        }
    }

    // ДОБАВЛЯЕМ: сохраняем позицию альбомов при изменении скролла
    LaunchedEffect(albumsListState.firstVisibleItemIndex) {
        if (currentAlbums.isNotEmpty()) {
            viewModel.setCurrentAlbumScrollIndex(albumsListState.firstVisibleItemIndex)
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
            when {
                isLoading && currentAlbums.isEmpty() && currentPhotos.isEmpty() -> {
                    // Показываем индикатор только при ПЕРВОНАЧАЛЬНОЙ загрузке
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                currentPhotos.isNotEmpty() -> {
                    PhotoGrid(
                        photos = currentPhotos,
                        isLoading = isLoading,
                        error = error,
                        onRetry = { viewModel.retry() },
                        onImageClick = { photo ->
                            val index = currentPhotos.indexOfFirst { it.id == photo.id }
                            if (index >= 0) {
                                viewModel.setCurrentPhotoIndex(index)
                            }
                            onImageClick(photo)
                        },
                        listState = photosListState
                    )
                }

                else -> {
                    AlbumGrid(
                        albums = currentAlbums,
                        onAlbumClick = { album ->
                            // Сохраняем позицию скролла перед переходом
                            val currentScrollIndex = albumsListState.firstVisibleItemIndex
                            viewModel.setCurrentAlbumScrollIndex(currentScrollIndex)

                            viewModel.navigateToAlbum(album)
                        },
                        listState = albumsListState
                    )
                }
            }
        }
    }
}