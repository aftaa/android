package ru.aftaa.p.mainactivity.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ru.aftaa.p.mainactivity.data.model.Photo

class AppNavigation : ViewModel() {
    var currentScreen by mutableStateOf<Screen>(Screen.Gallery)
        private set

    fun navigateTo(screen: Screen) {
        currentScreen = screen
    }

    fun back() {
        currentScreen = Screen.Gallery
    }
}

sealed class Screen {
    object Gallery : Screen()
    data class Detail(
        val initialImageIndex: Int,
        val photos: List<Photo>
    ) : Screen()
}