package ru.aftaa.p.mainactivity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.aftaa.p.mainactivity.components.DetailScreen
import ru.aftaa.p.mainactivity.data.model.Photo
import ru.aftaa.p.mainactivity.navigation.AppNavigation
import ru.aftaa.p.mainactivity.navigation.Screen
import ru.aftaa.p.mainactivity.screens.GalleryScreen

@Composable
fun MyGalleryApp() {
    val navigation = viewModel<AppNavigation>()
    val currentScreen = navigation.currentScreen // УБРАТЬ "by"

    when (currentScreen) {
        is Screen.Gallery -> {
            GalleryScreen(
                onPhotoClick = { clickedPhoto, allPhotos ->
                    val index = allPhotos.indexOfFirst { it.id == clickedPhoto.id }
                    navigation.navigateTo(Screen.Detail(index, allPhotos))
                }
            )
        }
        is Screen.Detail -> {
            val detailScreen = currentScreen as Screen.Detail
            DetailScreen(
                initialImageIndex = detailScreen.initialImageIndex,
                photos = detailScreen.photos,
                onBackClick = { navigation.back() }
            )
        }
    }
}