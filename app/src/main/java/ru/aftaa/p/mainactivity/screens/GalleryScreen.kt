package ru.aftaa.p.mainactivity.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.aftaa.p.mainactivity.components.AlbumGrid
import ru.aftaa.p.mainactivity.components.PhotoGrid
import ru.aftaa.p.mainactivity.data.model.Photo
import ru.aftaa.p.mainactivity.viewmodel.GalleryViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.activity.compose.BackHandler



// В GalleryScreen.kt
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
                    onImageClick = onImageClick
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