package ru.aftaa.p.mainactivity.navigation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ru.aftaa.p.mainactivity.data.model.Photo

class AppNavigation : ViewModel() {
    val currentScreen = mutableStateOf<Screen>(Screen.Gallery)

    fun navigateTo(screen: Screen) {
        currentScreen.value = screen
    }

    fun back() {
        currentScreen.value = Screen.Gallery
    }
}

sealed class Screen {
    object Gallery : Screen()
    data class Detail(
        val initialImageIndex: Int,
        val photos: List<Photo>
    ) : Screen()
}