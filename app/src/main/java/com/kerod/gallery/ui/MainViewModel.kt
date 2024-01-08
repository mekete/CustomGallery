package com.kerod.gallery.ui

import android.app.Application
import android.content.ContentResolver
import androidx.lifecycle.AndroidViewModel
import com.kerod.gallery.data.Media
import com.kerod.gallery.data.LocalDataProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(GalleryUiState(loading = true))
    val uiState: StateFlow<GalleryUiState> = _uiState



    private val contentResolver: ContentResolver = application.contentResolver

    private val _imageFolders: MutableStateFlow<List<Media>> = MutableStateFlow(emptyList())
    private val _videoFolders: MutableStateFlow<List<Media>> = MutableStateFlow(emptyList())
    private val _imageFiles: MutableStateFlow<List<Media>> = MutableStateFlow(emptyList())
    private val _videoFiles: MutableStateFlow<List<Media>> = MutableStateFlow(emptyList())

    //
    val imageFolders: StateFlow<List<Media>> = _imageFolders.asStateFlow()
    val videoFolders: StateFlow<List<Media>> = _videoFolders.asStateFlow()
    val imageFiles: StateFlow<List<Media>> = _imageFiles.asStateFlow()
    val videoFiles: StateFlow<List<Media>> = _videoFiles.asStateFlow()


    init {
        initFolderList()
    }

    private fun initFolderList() {
        val imageFolderList = LocalDataProvider.imageFolderList
        _uiState.value = GalleryUiState(
            videoFolders = imageFolderList,
            selectedMedia = imageFolderList.first()
        )
    }

    fun setSelectedCategoryAlbum(albumId: Long) {

        val selected = uiState.value.videoFolders.find { it.id == albumId }
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
    val selectedImage: List<Media> = emptyList(),
    val selectedVideo: List<Media> = emptyList(),
    val selectedMedia: Media? = null,
    val isDetailOnlyOpen: Boolean = false,
    val loading: Boolean = false,
    val error: String? = null
)
