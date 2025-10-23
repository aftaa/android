package ru.aftaa.p.mainactivity.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    val id: Long,
    val title: String,
    val thumbnailUrl: String,
    val fullImageUrl: String
) : Parcelable