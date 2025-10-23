package ru.aftaa.p.mainactivity.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ru.aftaa.p.mainactivity.data.model.Album

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlbumDropdownMenu(
    albums: List<Album>,
    selectedAlbum: Album?,
    onAlbumSelected: (Album?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Default.Menu, "Меню альбомов")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Все фотографии") },
                onClick = {
                    onAlbumSelected(null)
                    expanded = false
                }
            )

            Divider()

            albums.forEach { album ->
                DropdownMenuItem(
                    text = { Text("${album.title} (${album.photoCount})") },
                    onClick = {
                        onAlbumSelected(album)
                        expanded = false
                    }
                )
            }
        }
    }
}