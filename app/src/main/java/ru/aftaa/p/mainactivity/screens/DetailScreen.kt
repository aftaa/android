package ru.aftaa.p.mainactivity.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.aftaa.p.mainactivity.components.ZoomableImage
import ru.aftaa.p.mainactivity.data.model.Photo
import ru.aftaa.p.mainactivity.viewmodel.GalleryViewModel

@Composable
fun DetailScreen(
    initialImageIndex: Int,
    photos: List<Photo>,
    onBackClick: () -> Unit
) {
    val viewModel: GalleryViewModel = viewModel()

    BackHandler {
        onBackClick()
    }

    var currentPage by remember { mutableIntStateOf(initialImageIndex) }
    var isZoomed by remember { mutableStateOf(false) }

    val pagerState = rememberPagerState(
        initialPage = initialImageIndex,
        pageCount = { photos.size }
    )

    LaunchedEffect(pagerState.currentPage) {
        currentPage = pagerState.currentPage
        isZoomed = false
        viewModel.setCurrentPhotoIndex(pagerState.currentPage)
        println("📸 DEBUG: Сохраняем позицию ${pagerState.currentPage} в ViewModel")
    }

    // УБИРАЕМ Scaffold и TopAppBar
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Индикатор текущей позиции (опционально - можно убрать если хочешь чистый экран)
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
        ) {
            Text(
                text = "${currentPage + 1} / ${photos.size}",
                color = Color.White,
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            userScrollEnabled = !isZoomed
        ) { page ->
            ZoomableImage(
                imageUrl = photos[page].fullImageUrl,
                modifier = Modifier.fillMaxSize(),
                onZoomStateChange = { zoomed ->
                    isZoomed = zoomed
                }
            )
        }
    }
}