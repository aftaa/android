package ru.aftaa.p.mainactivity.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.ExperimentalFoundationApi // ИСПОЛЬЗУЙТЕ ЭТОТ ИМПОРТ
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.aftaa.p.mainactivity.data.model.Photo

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class) // ИСПОЛЬЗУЙТЕ ExperimentalFoundationApi
@Composable
fun DetailScreen(
    initialImageIndex: Int,
    photos: List<Photo>,
    onBackClick: () -> Unit
) {
    var currentPage by remember { mutableIntStateOf(initialImageIndex) }
    val pagerState = rememberPagerState(
        initialPage = initialImageIndex,
        pageCount = { photos.size }
    )

    LaunchedEffect(pagerState.currentPage) {
        currentPage = pagerState.currentPage
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "${currentPage + 1} / ${photos.size}",
                        style = androidx.compose.material3.MaterialTheme.typography.titleMedium
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
                modifier = Modifier.fillMaxSize()
            ) { page ->
                ZoomableImage(
                    imageUrl = photos[page].fullImageUrl,
                    modifier = Modifier.fillMaxSize()
                )
            }

            if (photos.size > 1) {
                PageIndicator(
                    pageCount = photos.size,
                    currentPage = currentPage,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp)
                )
            }
        }
    }
}

@Composable
fun PageIndicator(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        repeat(pageCount) { page ->
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .padding(2.dp)
                    .background(
                        color = if (page == currentPage) Color.White else Color.White.copy(alpha = 0.5f),
                        shape = CircleShape
                    )
            )
        }
    }
}