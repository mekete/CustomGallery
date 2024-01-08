package com.kerod.gallery.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kerod.gallery.data.Media
import com.kerod.gallery.ui.components.MediaItem
import com.kerod.gallery.ui.components.MediaListAppBar
import com.kerod.gallery.ui.components.ReplyEmailListItem
import com.kerod.gallery.ui.components.ReplySearchBar

@Composable
fun MediaListScreen(
    modifier: Modifier = Modifier,
    currentView: String = GalleryRoute.IMAGE,
    galleryUiState: GalleryUiState,
    closeMediaListScreen: () -> Unit,
    navigateToMediaListScreen: (Long) -> Unit,
) {

    val emailLazyListState = rememberLazyListState()

    Box(modifier = modifier.fillMaxSize()) {
        if (galleryUiState.selectedMedia != null && galleryUiState.isDetailOnlyOpen) {
            BackHandler {
                closeMediaListScreen()
            }
            val albumList = galleryUiState.selectedMedia.mediaFileList
            MediaListScreen(mediaList = albumList, albumName = galleryUiState.selectedMedia.fileName, albumSize = galleryUiState.selectedMedia.size) {
                closeMediaListScreen()
            }
        } else {
            val albums = if (currentView == GalleryRoute.IMAGE) {
                galleryUiState.videoFolders
            } else {
                galleryUiState.videoFolders
            }
            FolderListScreen(albumList = albums, emailLazyListState = emailLazyListState, modifier = modifier, navigateToDetail = navigateToMediaListScreen)
        }
    }
}


@Composable
fun FolderListScreen(
    modifier: Modifier = Modifier, albumList: List<Media>, emailLazyListState: LazyListState, selectedMedia: Media? = null, navigateToDetail: (Long) -> Unit
) {
    LazyColumn(modifier = modifier, state = emailLazyListState) {
        item {
            ReplySearchBar(modifier = Modifier.fillMaxWidth())
        }
        items(items = albumList, key = { it.id }) { email ->
            ReplyEmailListItem(album = email, isSelected = email.id == selectedMedia?.id) { emailId ->
                navigateToDetail(emailId)
            }
        }
    }
}

@Composable
fun MediaListScreen(
    modifier: Modifier = Modifier, albumName: String, albumSize: Int, mediaList: List<Media>, isFullScreen: Boolean = true, onBackPressed: () -> Unit = {}
) {
    LazyColumn(modifier = modifier.padding(top = 16.dp)) {
        item {
            MediaListAppBar(albumName, albumSize, isFullScreen) {
                onBackPressed()
            }
        }
        items(items = mediaList, key = { it.id }) { media ->
            MediaItem(album = media)
        }
    }
}
