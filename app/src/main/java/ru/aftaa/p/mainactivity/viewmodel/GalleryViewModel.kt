package ru.aftaa.p.mainactivity.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.aftaa.p.mainactivity.data.model.Album
import ru.aftaa.p.mainactivity.data.model.Photo
import ru.aftaa.p.mainactivity.network.RetrofitClient

class GalleryViewModel : ViewModel() {
    // Используем mutableStateOf напрямую без State интерфейса
    val currentAlbums = mutableStateOf<List<Album>>(emptyList())
    val currentPhotos = mutableStateOf<List<Photo>>(emptyList())
    val isLoading = mutableStateOf(false)
    val error = mutableStateOf<String?>(null)
    val currentAlbumTitle = mutableStateOf("Альбомы")
    val canGoBack = mutableStateOf(false)

    private val navigationStack = mutableListOf<Album>()
    private val _albumsTree = mutableStateOf<List<Album>>(emptyList())

    init {
        loadAlbumsTree()
        updateCanGoBack()
    }

    private fun loadAlbumsTree() {
        isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getAlbumsTree()
                if (response.success) {
                    _albumsTree.value = response.data
                    currentAlbums.value = response.data
                    error.value = null
                } else {
                    error.value = "Ошибка загрузки альбомов"
                }
            } catch (e: Exception) {
                error.value = "Ошибка сети: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun navigateToAlbum(album: Album) {
        if (album.childAlbums.isNotEmpty()) {
            navigationStack.add(album)
            currentAlbums.value = album.childAlbums
            currentPhotos.value = emptyList()
            currentAlbumTitle.value = album.title
        } else {
            loadAlbumPhotos(album)
        }
        updateCanGoBack()
    }

    private fun loadAlbumPhotos(album: Album) {
        isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getAlbumPhotos(album.id)
                if (response.success) {
                    currentPhotos.value = response.data
                    currentAlbums.value = emptyList()
                    navigationStack.add(album)
                    currentAlbumTitle.value = album.title
                    error.value = null
                } else {
                    error.value = "Ошибка загрузки фото"
                }
            } catch (e: Exception) {
                error.value = "Ошибка сети: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun goBack() {
        println("DEBUG: goBack called, stack size: ${navigationStack.size}")

        if (navigationStack.isNotEmpty()) {
            navigationStack.removeLast()
            currentPhotos.value = emptyList()

            if (navigationStack.isNotEmpty()) {
                val parentAlbum = navigationStack.last()
                currentAlbums.value = parentAlbum.childAlbums
                currentAlbumTitle.value = parentAlbum.title
            } else {
                currentAlbums.value = _albumsTree.value
                currentAlbumTitle.value = "Альбомы"
            }
        } else {
            println("DEBUG: Already at root, doing nothing")
            // Остаемся на главной, приложение не закрывается
        }
        updateCanGoBack()
    }

    private fun updateCanGoBack() {
        canGoBack.value = navigationStack.isNotEmpty()
    }

    fun retry() {
        if (currentPhotos.value.isNotEmpty()) {
            navigationStack.lastOrNull()?.let { loadAlbumPhotos(it) }
        } else {
            loadAlbumsTree()
        }
    }
}