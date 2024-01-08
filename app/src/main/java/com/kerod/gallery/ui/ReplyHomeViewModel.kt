package com.kerod.gallery.ui

import androidx.lifecycle.ViewModel
import com.kerod.gallery.data.Media
import com.kerod.gallery.data.LocalEmailsDataProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
class ReplyHomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GalleryUiState(loading = true))
    val uiState: StateFlow<GalleryUiState> = _uiState


    init {
        initFolderList()
    }

    private fun initFolderList() {
        val mediaList = LocalEmailsDataProvider.imageFolderList
        _uiState.value = GalleryUiState(
            videoFolders = mediaList,
            selectedMedia = mediaList.first()
        )
    }

    fun setSelectedCategoryAlbum(bucketId: Long, bucketLabel:String, size:Int, type:String) {

        val selected = uiState.value.videoFolders.find { it.id == bucketId }
        _uiState.value = _uiState.value.copy(
            selectedMedia = selected,
            isDetailOnlyOpen = true
        )
    }

    fun closeDetailScreen() {
        _uiState.value = _uiState
            .value.copy(
                isDetailOnlyOpen = false,
                selectedMedia = _uiState.value.videoFolders.first()
            )
    }
}

data class GalleryUiState(
    val imageFolders: List<Media> = emptyList(),
    val videoFolders: List<Media> = emptyList(),

    val imageList: List<Media> = emptyList(),
    val videoList: List<Media> = emptyList(),

    val selectedType: String = GalleryRoute.IMAGE,
    val selectedBucketId: Long = 1L,
    val selectedMedia: Media? = null,
    val isDetailOnlyOpen: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
)
