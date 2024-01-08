package com.kerod.gallery.ui

import android.app.Application
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.kerod.gallery.state.GalleryUiState
import com.kerod.gallery.R

@Composable
fun GalleryContent(
    modifier: Modifier = Modifier, application : Application, galleryUiState: GalleryUiState, closeFileListScreen: () -> Unit = {}, navigateToFileListScreen: (Long, String, Int, String) -> Unit = { bucketId, bucketLabel, size, type->

    }
) {
    Surface(tonalElevation = 5.dp) {


        val selectedFolder = remember { mutableStateOf(GalleryRoute.IMAGE) }

        Column(modifier = modifier.fillMaxSize()) {

            when (selectedFolder.value) {
                GalleryRoute.IMAGE -> {
                    MediaListScreen(currentView = GalleryRoute.IMAGE, galleryUiState = galleryUiState, closeMediaListScreen = closeFileListScreen, navigateToMediaListScreen = navigateToFileListScreen, modifier = Modifier.weight(1f))
                }
                GalleryRoute.MOVIE -> {
                    MediaListScreen(currentView = GalleryRoute.MOVIE, galleryUiState = galleryUiState, closeMediaListScreen = closeFileListScreen, navigateToMediaListScreen = navigateToFileListScreen, modifier = Modifier.weight(1f))
                }
                else -> {
                    SettingsScreen(currentView = GalleryRoute.SETTING, galleryUiState = galleryUiState, closeMediaListScreen = closeFileListScreen, navigateToMediaListScreen = navigateToFileListScreen, modifier = Modifier.weight(1f))
                }
            }

            NavigationBar(modifier = Modifier.fillMaxWidth()) {
                TOP_LEVEL_DESTINATIONS.forEach { destination ->
                    NavigationBarItem(selected = selectedFolder.value == destination.route,
                        onClick = { selectedFolder.value = destination.route },
                        icon = { Icon(imageVector = destination.selectedIcon, contentDescription = stringResource(id = destination.iconTextId)) },
                        label = { Text(text = stringResource(id = destination.iconTextId))}
                    )
                }
            }
        }
    }
}


object GalleryRoute {
    const val IMAGE = "Image"
    const val MOVIE = "Movies"
    const val SETTING = "Setting"
}

data class TopLevelDestination(
    val route: String, val selectedIcon: ImageVector, val unselectedIcon: ImageVector, val iconTextId: Int
)

val TOP_LEVEL_DESTINATIONS =
    listOf(
        TopLevelDestination(route = GalleryRoute.IMAGE, selectedIcon = Icons.Default.Image, unselectedIcon = Icons.Outlined.Image, iconTextId = R.string.tab_image),
        TopLevelDestination(route = GalleryRoute.MOVIE, selectedIcon = Icons.Default.Movie, unselectedIcon = Icons.Outlined.Movie, iconTextId = R.string.tab_movie),
        TopLevelDestination(route = GalleryRoute.SETTING, selectedIcon = Icons.Default.Settings, unselectedIcon = Icons.Outlined.Settings, iconTextId = R.string.tab_setting))