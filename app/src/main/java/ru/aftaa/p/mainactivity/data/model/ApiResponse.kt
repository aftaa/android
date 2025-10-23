package ru.aftaa.p.mainactivity.data.model

data class AlbumsTreeResponse(
    val success: Boolean,
    val data: List<Album>
)

data class AlbumPhotosResponse(
    val success: Boolean,
    val data: List<Photo>
)