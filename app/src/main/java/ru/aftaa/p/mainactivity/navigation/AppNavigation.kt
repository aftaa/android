package ru.aftaa.p.mainactivity.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.aftaa.p.mainactivity.components.AlbumGrid
import ru.aftaa.p.mainactivity.components.DetailScreen
import ru.aftaa.p.mainactivity.screens.GalleryScreen
import ru.aftaa.p.mainactivity.components.PhotoGrid
import ru.aftaa.p.mainactivity.data.model.Photo
import ru.aftaa.p.mainactivity.viewmodel.GalleryViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "gallery"
    ) {
        composable("gallery") {
            val viewModel: GalleryViewModel = hiltViewModel()
            GalleryScreen(
                viewModel = viewModel,
                onImageClick = { photo ->
                    // Временное решение - нужно доработать чтобы получить albumId и index
                    navController.navigate("detail/temp_album?startIndex=0")
                }
            )
        }

        // Этот маршрут может не понадобиться, если GalleryScreen сам управляет навигацией
        // Но оставим на всякий случай
        composable("album/{albumId}") { backStackEntry ->
            val albumId = backStackEntry.arguments?.getString("albumId") ?: ""
            val viewModel: GalleryViewModel = hiltViewModel()

            // Если GalleryScreen сам переходит в состояние показа фото,
            // этот экран может не использоваться
            PhotoGrid(
                photos = viewModel.photos,
                isLoading = viewModel.isLoading,
                error = viewModel.error,
                onRetry = { viewModel.loadPhotosByAlbum(albumId) },
                onImageClick = { photo, index ->
                    navController.navigate("detail/${albumId}?startIndex=$index")
                },
                albumId = albumId,
                viewModel = viewModel
            )
        }

        composable(
            "detail/{albumId}?startIndex={startIndex}",
            arguments = listOf(
                androidx.navigation.NavArgument(
                    name = "albumId",
                    builder = { androidx.navigation.NavType.StringType }
                ),
                androidx.navigation.NavArgument(
                    name = "startIndex",
                    builder = {
                        androidx.navigation.NavType.IntType
                        defaultValue = 0
                        nullable = false
                    }
                )
            )
        ) { backStackEntry ->
            val albumId = backStackEntry.arguments?.getString("albumId") ?: ""
            val startIndex = backStackEntry.arguments?.getInt("startIndex") ?: 0
            val viewModel: GalleryViewModel = hiltViewModel()

            // Получаем фото для альбома
            val photos = viewModel.getPhotosByAlbum(albumId)

            DetailScreen(
                initialImageIndex = startIndex,
                albumId = albumId,
                photos = photos,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}