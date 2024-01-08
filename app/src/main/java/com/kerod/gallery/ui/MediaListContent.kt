package com.kerod.gallery.ui

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kerod.gallery.FolderListViewModel
import com.kerod.gallery.R
import com.kerod.gallery.data.Media
import com.kerod.gallery.ui.components.FolderItem
import com.kerod.gallery.ui.components.CommonFolder
import com.kerod.gallery.ui.components.MediaItem
import com.kerod.gallery.ui.components.MediaListAppBar
import com.kerod.gallery.ui.components.ReplySearchBar

private const val TAG = "MediaListContent"

@Composable
fun MediaListScreen(
    modifier: Modifier = Modifier,
    currentView: String = GalleryRoute.IMAGE,
    galleryUiState: GalleryUiState,
    closeMediaListScreen: () -> Unit,
    navigateToMediaListScreen: (Long, String, Int,String) -> Unit,
) {

    val emailLazyListState = rememberLazyListState()

    Box(modifier = modifier.fillMaxSize()) {
        if (galleryUiState.selectedMedia != null && galleryUiState.showFilesInsideFolder) {
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
            FolderListScreen(albumList = albums, currentView = currentView, emailLazyListState = emailLazyListState, modifier = modifier, navigateToDetail = navigateToMediaListScreen)
        }
    }
}


@Composable
fun FolderListContent(viewModel: FolderListViewModel = viewModel()) {
    val folders by viewModel.imageFolders.collectAsState()
    Log.e(TAG, "FolderListContent: ${folders.size}")

    LazyColumn {
        items(folders) { folder ->
            Text(text = folder.bucketDisplayName)
            Log.e(TAG, "FolderListContent: ${folder.bucketDisplayName}")
        }
    }
}

@Composable
fun FolderListScreen(
    modifier: Modifier = Modifier, viewModel: FolderListViewModel = viewModel(), currentView: String, albumList: List<Media>, emailLazyListState: LazyListState, selectedMedia: Media? = null, navigateToDetail: (Long, String, Int,String) -> Unit
) {

    val imageFolders by if (GalleryRoute.MOVIE.equals(currentView)) {
        viewModel.videoFolders.collectAsState()
    } else {
        viewModel.imageFolders.collectAsState()
    }
    LazyColumn(modifier = modifier, state = emailLazyListState) {
        item {
            ReplySearchBar(modifier = Modifier.fillMaxWidth())
        }
        item {
            Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(92.dp)
                    .padding(bottom = 16.dp), horizontalArrangement = Arrangement.SpaceBetween) { //                //all movies
                CommonFolder(modifier = modifier, currentView = currentView, labelResource = if(currentView == GalleryRoute.IMAGE){R.string.all_image}else{R.string.all_video}, icon = if(currentView == GalleryRoute.IMAGE){Icons.Default.Image}else{Icons.Default.VideoFile} ,  isSelected = false) { bucketId, bucketLabel, size, type ->
                    navigateToDetail(bucketId , bucketLabel , size , type)
                } //all
                CommonFolder(modifier = modifier, currentView = currentView, labelResource = R.string.favorites, icon = Icons.Default.Favorite, isSelected = true) { bucketId , bucketLabel , size , type ->
                    navigateToDetail(bucketId , bucketLabel , size , type)
                }
            }
        }

        items(items = imageFolders, key = { it.id }) { album ->
            FolderItem(album = album, currentView = currentView, isSelected = album.id == selectedMedia?.id) { bucketId, bucketLabel, size, type ->
                navigateToDetail(bucketId , bucketLabel , size , type)
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
