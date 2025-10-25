package ru.aftaa.p.mainactivity.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.aftaa.p.mainactivity.data.model.Album

@Composable
fun AlbumGrid(
    albums: List<Album>,
    onAlbumClick: (Album) -> Unit,
    modifier: Modifier = Modifier
) {
    println("DEBUG: AlbumGrid rendering ${albums.size} albums")

    LazyVerticalGrid(
        columns = GridCells.Fixed(3), // Фиксированные 3 колонки
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(albums) { album ->
            println("DEBUG: Rendering album: ${album.title}")
            AlbumItem(
                album = album,
                onAlbumClick = onAlbumClick
            )
        }
    }
}

@Composable
fun AlbumItem(
    album: Album,
    onAlbumClick: (Album) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { onAlbumClick(album) },
        elevation = 0.dp, // Без тени
        backgroundColor = androidx.compose.ui.graphics.Color.Transparent, // Прозрачный фон
        shape = RoundedCornerShape(0.dp) // Без скруглений
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Превью альбома
            AsyncImage(
                model = album.coverPhotoUrl ?: "https://picsum.photos/100/100",
                contentDescription = album.title,
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Название альбома по центру
            Text(
                text = album.title,
                style = androidx.compose.material.MaterialTheme.typography.subtitle1,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }
    }
}