
package ru.aftaa.p.mainactivity

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.MaterialTheme
import ru.aftaa.p.mainactivity.navigation.AppNavigation

@Composable
fun MyGalleryApp() {
    MaterialTheme {  // ← СТАНДАРТНАЯ ТЕМА
        AppNavigation()
    }
}

@Preview
@Composable
fun MyGalleryAppPreview() {
    MyGalleryApp()
}