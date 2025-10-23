package ru.aftaa.p.mainactivity.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.aftaa.p.mainactivity.data.model.Album
import ru.aftaa.p.mainactivity.data.model.Photo
import ru.aftaa.p.mainactivity.network.RetrofitClient

class GalleryViewModel : ViewModel() {
    private val _albumsTree = mutableStateOf<List<Album>>(emptyList())
    private val _currentAlbums = mutableStateOf<List<Album>>(emptyList())
    private val _currentPhotos = mutableStateOf<List<Photo>>(emptyList())
    private val _isLoading = mutableStateOf(false)
    private val _error = mutableStateOf<String?>(null)

    private val navigationStack = mutableStateListOf<Album>()
    private val _currentAlbumTitle = mutableStateOf("Альбомы")

    // Public states
    val currentAlbums: State<List<Album>> = _currentAlbums
    val currentPhotos: State<List<Photo>> = _currentPhotos
    val isLoading: State<Boolean> = _isLoading
    val error: State<String?> = _error
    val currentAlbumTitle: State<String> = _currentAlbumTitle

    val canGoBack: State<Boolean> = mutableStateOf(navigationStack.isNotEmpty())

    init {
        loadAlbumsTree()
    }

    private fun loadAlbumsTree() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getAlbumsTree()
                if (response.success) {
                    _albumsTree.value = response.data
                    _currentAlbums.value = response.data
                    _error.value = null
                } else {
                    _error.value = "Ошибка загрузки альбомов"
                }
            } catch (e: Exception) {
                _error.value = "Ошибка сети: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun navigateToAlbum(album: Album) {
        if (album.childAlbums.isNotEmpty()) {
            // Переход к вложенным альбомам
            navigationStack.add(album)
            _currentAlbums.value = album.childAlbums
            _currentPhotos.value = emptyList()
            _currentAlbumTitle.value = album.title
        } else if (album.hasPhotos) {
            // Загрузка фото конечного альбома
            loadAlbumPhotos(album)
        }
    }

    private fun loadAlbumPhotos(album: Album) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.instance.getAlbumPhotos(album.id)
                if (response.success) {
                    _currentPhotos.value = response.data
                    _currentAlbums.value = emptyList()
                    navigationStack.add(album)
                    _currentAlbumTitle.value = album.title
                    _error.value = null
                } else {
                    _error.value = "Ошибка загрузки фото"
                }
            } catch (e: Exception) {
                _error.value = "Ошибка сети: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun goBack() {
        _currentPhotos.value = emptyList()
        if (navigationStack.isNotEmpty()) {
            navigationStack.removeLast()
            val parentAlbum = navigationStack.lastOrNull()
            _currentAlbums.value = parentAlbum?.childAlbums ?: _albumsTree.value
            _currentAlbumTitle.value = parentAlbum?.title ?: "Альбомы"
        }
    }

    fun retry() {
        if (_currentPhotos.value.isNotEmpty()) {
            // Перезагружаем фото
            navigationStack.lastOrNull()?.let { loadAlbumPhotos(it) }
        } else {
            // Перезагружаем дерево альбомов
            loadAlbumsTree()
        }
    }
}