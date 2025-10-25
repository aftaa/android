package ru.aftaa.p.mainactivity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.aftaa.p.mainactivity.components.DetailScreen
import ru.aftaa.p.mainactivity.data.model.Photo
import ru.aftaa.p.mainactivity.navigation.AppNavigation
import ru.aftaa.p.mainactivity.navigation.Screen
import ru.aftaa.p.mainactivity.screens.GalleryScreen
import ru.aftaa.p.mainactivity.viewmodel.GalleryViewModel

@Composable
fun MyGalleryApp() {
    val navigation = viewModel<AppNavigation>()
    val currentScreen = navigation.currentScreen.value

    when (currentScreen) {
        is Screen.Gallery -> {
            val galleryViewModel: GalleryViewModel = viewModel()

            GalleryScreen(
                viewModel = galleryViewModel,
                onImageClick = { clickedPhoto ->
                    val allPhotos = galleryViewModel.currentPhotos.value
                    val index = allPhotos.indexOfFirst { it.id == clickedPhoto.id }
                    navigation.navigateTo(Screen.Detail(index, allPhotos))
                }
            )
        }
        is Screen.Detail -> {
            DetailScreen(
                initialImageIndex = currentScreen.initialImageIndex,
                photos = currentScreen.photos,
                onBackClick = { navigation.back() } // Это вернет к GalleryScreen
            )
        }
    }
}