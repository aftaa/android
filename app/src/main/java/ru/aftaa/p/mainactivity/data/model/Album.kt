package ru.aftaa.p.mainactivity.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Album(
    val id: Long,
    val title: String,
    val coverPhotoUrl: String?,
    val photoCount: Int,
    val childAlbums: List<Album> = emptyList(),
    val hasPhotos: Boolean = false
) : Parcelable