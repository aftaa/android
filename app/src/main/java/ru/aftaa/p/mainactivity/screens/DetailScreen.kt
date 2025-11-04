package ru.aftaa.p.mainactivity.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.aftaa.p.mainactivity.components.ZoomableImage
import ru.aftaa.p.mainactivity.data.model.Photo
import ru.aftaa.p.mainactivity.viewmodel.GalleryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    initialImageIndex: Int,
    photos: List<Photo>,
    onBackClick: () -> Unit
) {
    val viewModel: GalleryViewModel = viewModel() // ДОБАВЛЯЕМ ViewModel

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "${currentPage + 1} / ${photos.size}",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black)
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                userScrollEnabled = !isZoomed // ← Блокируем свайпы когда увеличены
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
}