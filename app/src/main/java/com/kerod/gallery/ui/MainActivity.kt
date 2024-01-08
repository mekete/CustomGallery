
package com.kerod.gallery.ui

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kerod.gallery.ui.theme.AppTheme

class MainActivity : ComponentActivity() {

    private val viewModel: ReplyHomeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val LocalApplication = compositionLocalOf<Application> {
                error("CompositionLocal LocalApplication not provided")
            }

            requestPermission(this)

            AppTheme {
                 Surface(tonalElevation = 5.dp) {

                     val application = Application()
                     CompositionLocalProvider(LocalApplication provides application) {
//                         MediaUtil().listAllMediaFolders(context = LocalContext.current, GalleryRoute.IMAGE)

                         GalleryContent(
                             galleryUiState = uiState,
                             application = application,
                             closeFileListScreen = { viewModel.closeDetailScreen() },
                             navigateToFileListScreen = { bucketId , bucketLabel , size , type ->
                                 viewModel.setSelectedCategoryAlbum(bucketId , bucketLabel , size , type)
                             }
                         )                     }
                }
            }
        }
    }

    private fun requestPermission(context: Activity) {

        if (ContextCompat.checkSelfPermission(  context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission( context, android.Manifest.permission.MANAGE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission( context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED

        ) {
            ActivityCompat.requestPermissions(
                context,
                arrayOf(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    //android.Manifest.permission.MANAGE_EXTERNAL_STORAGE
                ),
                19
            )
        }
    }
    fun checkAndRequestLocationPermissions(
        context: Context,
        permissions: Array<String>,
        launcher: ManagedActivityResultLauncher<Array<String>, Map<String, Boolean>>
    ) {
        if (
            permissions.all {
                ContextCompat.checkSelfPermission(
                    context,
                    it
                ) == PackageManager.PERMISSION_GRANTED
            }
        ) {
            // Use location because permissions are already granted
        } else {
            // Request permissions
            launcher.launch(permissions)
        }
    }
}
