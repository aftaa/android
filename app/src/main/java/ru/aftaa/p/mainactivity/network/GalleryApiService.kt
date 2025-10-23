package ru.aftaa.p.mainactivity.network

import retrofit2.http.GET
import retrofit2.http.Path
import ru.aftaa.p.mainactivity.data.model.AlbumsTreeResponse
import ru.aftaa.p.mainactivity.data.model.AlbumPhotosResponse

interface GalleryApiService {
    @GET("albums/tree")
    suspend fun getAlbumsTree(): AlbumsTreeResponse

    @GET("albums/{albumId}/photos")
    suspend fun getAlbumPhotos(
        @Path("albumId") albumId: Long
    ): AlbumPhotosResponse
}