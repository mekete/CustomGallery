package com.kerod.gallery.ui

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Intent
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
import com.kerod.gallery.state.FolderListViewModel
import com.kerod.gallery.ui.theme.AppTheme
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

class MainActivity : ComponentActivity() {

    private val viewModel: FolderListViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 23)

        setContent {

            var isPermissionGranted by remember { mutableStateOf(false) }
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            val LocalApplication = compositionLocalOf<Application> {
                error("CompositionLocal LocalApplication not provided")
            }

            AppTheme {
                Surface(tonalElevation = 5.dp) {

                    val application = Application()
                    CompositionLocalProvider(LocalApplication provides application) { //                         MediaUtil().listAllMediaFolders(context = LocalContext.current, GalleryRoute.IMAGE)
                        RequestPermission(  )

                        GalleryContent(galleryUiState = uiState, application = application, closeFileListScreen = { viewModel.closeDetailScreen() }, navigateToFileListScreen = { bucketId, bucketLabel, fileCount, imageOrVideo ->
                            viewModel.setSelectedBucket(bucketId, bucketLabel, fileCount, imageOrVideo)
                        })
                    }

                }
            }

        }

    }
    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    private fun RequestPermission(  ) {

        var showDialog by remember { mutableStateOf(false) }
        val oldPermissionState = rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE, onPermissionResult = {   })
//        val imagePermissionState = rememberPermissionState(Manifest.permission.READ_MEDIA_IMAGES, onPermissionResult = {})
//        val videoPermissionState = rememberPermissionState(Manifest.permission.READ_MEDIA_VIDEO, onPermissionResult = {})

        val activity= LocalContext.current as Activity

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            if (!oldPermissionState.status.isGranted) {
            //show dialog oldPermissionState.launchPermissionRequest()
                showDialog=true
            }

        } else {
            val imagePermissionState = rememberPermissionState(Manifest.permission.READ_MEDIA_IMAGES, onPermissionResult = {})
            val videoPermissionState = rememberPermissionState(Manifest.permission.READ_MEDIA_VIDEO, onPermissionResult = {})

            if (!imagePermissionState.status.isGranted || !videoPermissionState.status.isGranted) { //cameraPermissionState.launchPermissionRequest()
                showDialog=true
            }
        }

        if (showDialog) {
            LaunchedEffect(showDialog) {
                showDialog = false // Reset the state after showing the dialog
            }
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                title = {
                    Text(text = "Permission Required")
                },
                text = {
                    Text("Issue on presenting permission dialog...tatus\t1) Go to settings and grant permission\n\t2) Kill the app and restart it.")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            Log.e("TAG", "RequestPermission: XXXXX ", )
                            showDialog = false
                            activity .openAppSettings()
                        }
                    ) {
                        Text("Settings")
                    }
                }
            )
        }


    }

    fun Activity.openAppSettings() {
        val intent = Intent().apply {
            action = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                }
                else -> {
                    Intent.ACTION_APPLICATION_PREFERENCES
                }
            }
            data = android.net.Uri.parse("package:$packageName")
        }
        startActivity(intent)
    }

}
