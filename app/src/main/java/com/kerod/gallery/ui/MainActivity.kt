
package com.kerod.gallery.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kerod.gallery.ui.theme.AppTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            AppTheme {
                Surface(tonalElevation = 5.dp) {
                    GalleryApp(
                        galleryUiState = uiState,
                        closeFileListScreen = { viewModel.closeDetailScreen() },
                        navigateToFileListScreen = { screen ->viewModel.setSelectedCategoryAlbum(screen)}
                    )
                }
            }
        }
    }
}

//@Preview(
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    name = "DefaultPreviewDark"
//)
//@Preview(
//    uiMode = Configuration.UI_MODE_NIGHT_NO,
//    name = "DefaultPreviewLight"
//)
//@Composable
//fun ReplyAppPreview() {
//    AppTheme {
//        GalleryApp(
//            galleryUiState = GalleryUiState(
//                albums = LocalDataProvider.imageFolderList
//            )
//        )
//    }
//}
