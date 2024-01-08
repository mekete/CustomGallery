package com.kerod.gallery.ui

import androidx.lifecycle.ViewModel
import com.kerod.gallery.data.Media
import com.kerod.gallery.data.LocalEmailsDataProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GalleryUiState(loading = true))
    val uiState: StateFlow<GalleryUiState> = _uiState

    init {
        initFolderList()
    }

    private fun initFolderList() {
        val mediaList = LocalEmailsDataProvider.imageFolderList
        _uiState.value = GalleryUiState(
            albumFolders = mediaList,
            selectedMedia = mediaList.first()
        )
    }

    fun setSelectedCategoryAlbum(albumId: Long) {

        val selected = uiState.value.albumFolders.find { it.id == albumId }
        _uiState.value = _uiState.value.copy(
            selectedMedia = selected,
            isDetailOnlyOpen = true
        )
    }

    fun closeDetailScreen() {
        _uiState.value = _uiState
            .value.copy(
                isDetailOnlyOpen = false,
                selectedMedia = _uiState.value.albumFolders.first()
            )
    }
}

data class GalleryUiState(
    val imageFolders: List<Media> = emptyList(),
    val albumFolders: List<Media> = emptyList(),
    val selectedMedia: Media? = null,
    val isDetailOnlyOpen: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
)
