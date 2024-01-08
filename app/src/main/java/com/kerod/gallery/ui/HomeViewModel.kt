package com.kerod.gallery.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import com.kerod.gallery.data.LocalEmailsDataProvider
import com.kerod.gallery.data.Media
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private const val TAG = "HomeViewModel"
class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GalleryUiState(loading = true))
    val uiState: StateFlow<GalleryUiState> = _uiState


    init {
        initFolderList()
    }

    private fun initFolderList() {
        val mediaList = LocalEmailsDataProvider.imageFolderList
        _uiState.value = GalleryUiState(videoFolders = mediaList, selectedMedia = mediaList.first())
    }

    fun setSelectedBucket(bucketId: Long, bucketLabel: String, size: Int, type: String) {

        val selectedFolder = if (GalleryRoute.IMAGE == type) {
            uiState.value.imageFolders.find { it.id == bucketId }
        } else if (GalleryRoute.MOVIE == type) {
            uiState.value.videoFolders.find { it.id == bucketId }
        } else {
            null
        }
        Log.e(TAG, "setSelectedCategoryAlbum: ", )

        _uiState.value = _uiState.value.copy(numberOfMediaFiles = size, folderName = bucketLabel, selectedBucketId = bucketId, selectedType = type, selectedMedia = selectedFolder, showFilesInsideFolder = true)
    }

    fun closeDetailScreen() {
        _uiState.value = _uiState.value.copy(
                    showFilesInsideFolder = false,
                    selectedMedia = _uiState.value.videoFolders.first(),
                    selectedBucketId = -1,

    )
    }
}

data class GalleryUiState(
    val imageFolders: List<Media> = emptyList(), val videoFolders: List<Media> = emptyList(),

    val imageList: List<Media> = emptyList(), val videoList: List<Media> = emptyList(),

    val selectedType: String = GalleryRoute.IMAGE, val numberOfMediaFiles: Int = 1, val folderName: String = "", val selectedBucketId: Long = 1L, val selectedMedia: Media? = null, val showFilesInsideFolder: Boolean = false, val loading: Boolean = false, val error: String? = null
)
