package com.kerod.gallery.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kerod.gallery.R
import com.kerod.gallery.ui.components.MediaListAppBar

@Composable
fun EmptyComingSoon(

    modifier: Modifier = Modifier,
    currentView: String = GalleryRoute.IMAGE,
    galleryUiState: GalleryUiState,
    closeMediaListScreen: () -> Unit,
    navigateToMediaListScreen: (Long) -> Unit,

    ) {
    Box(modifier = modifier.fillMaxSize()) {
        BackHandler {
            closeMediaListScreen()
        } //val albumList = galleryUiState.selectedMedia?.mediaFileList


        MediaListAppBar(albumName = "aa", albumSize = 9999, isFullScreen = false, onBackPressed = closeMediaListScreen)

        Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(modifier = Modifier.padding(8.dp), text = stringResource(id = R.string.empty_screen_title), style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.primary)
            Text(modifier = Modifier.padding(horizontal = 8.dp), text = stringResource(id = R.string.empty_screen_subtitle), style = MaterialTheme.typography.bodySmall, textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.outline)
        }
    }
}

@Preview
@Composable
fun ComingSoonPreview() { //EmptyComingSoon()
}
