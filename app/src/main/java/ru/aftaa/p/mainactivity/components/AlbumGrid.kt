package ru.aftaa.p.mainactivity.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
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
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(8.dp)
    ) {
        items(albums) { album ->
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
            .padding(8.dp)
            .clickable { onAlbumClick(album) },
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Превью альбома
            AsyncImage(
                model = album.coverPhotoUrl ?: "https://picsum.photos/100/100",
                contentDescription = album.title,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    text = album.title,
                    style = androidx.compose.material.MaterialTheme.typography.h6,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${album.photoCount} фото",
                    style = androidx.compose.material.MaterialTheme.typography.body2,
                    color = androidx.compose.material.MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                )
            }

            // Стрелка для вложенных альбомов
            if (album.childAlbums.isNotEmpty()) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowRight,
                    contentDescription = "Вложенные альбомы"
                )
            }
        }
    }
}