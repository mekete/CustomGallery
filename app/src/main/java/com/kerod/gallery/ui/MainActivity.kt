package com.kerod.gallery.ui

import android.Manifest
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kerod.gallery.FolderListViewModel
import com.kerod.gallery.ui.theme.AppTheme

class MainActivity : ComponentActivity() {

    private val viewModel: FolderListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 23)

        setContent {

            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val LocalApplication = compositionLocalOf<Application> {
                error("CompositionLocal LocalApplication not provided")
            }


            AppTheme {
                Surface(tonalElevation = 5.dp) {

                    val application = Application()
                    CompositionLocalProvider(LocalApplication provides application) { //                         MediaUtil().listAllMediaFolders(context = LocalContext.current, GalleryRoute.IMAGE)

                        GalleryContent(galleryUiState = uiState, application = application, closeFileListScreen = { viewModel.closeDetailScreen() }, navigateToFileListScreen = { bucketId, bucketLabel, fileCount, imageOrVideo ->
                            viewModel.setSelectedBucket(bucketId, bucketLabel, fileCount, imageOrVideo)
                        })
                    }
                }
            }
        }
    }

}
