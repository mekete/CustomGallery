package com.kerod.gallery.ui

import android.content.ContentResolver
import android.content.Context
import android.os.Build
import android.provider.MediaStore
import com.kerod.gallery.data.OrmUtl
import com.kerod.gallery.data.projection
import java.util.concurrent.TimeUnit

private const val TAG = "MediaUtil"

class MediaUtil {


    fun listVideos(context: Context) {

        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        }


        // Show only videos that are at least 5 minutes in duration.
        val selectionImage = "${MediaStore.Images.Media.DATE_ADDED} >= ?"
        val selectionVideo = "${MediaStore.Video.Media.DURATION} >= ?"
        val selectionArgs = arrayOf(TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES).toString())

        // Display videos in alphabetical order based on their display name.
        val sortOrder = "${MediaStore.Video.Media.DISPLAY_NAME} ASC"
        val sortOrderFolder = "${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} ASC"

        //Call the query() method in a worker thread.
        val query = context.contentResolver.query(collection, projection, selectionVideo, selectionArgs, sortOrder)
        val vidsList= OrmUtl().  cursorToMediaList(query)

    }



    private fun listCommonMediaFoldersWithLabels(context: Context): List<Pair<String, String>> {
        val contentResolver = context.contentResolver

        val commonFolderMappings = mapOf(
            "DCIM/Camera" to "Camera",
            "Pictures/Screenshots" to "Screenshots", // Add more mappings as needed
        )

        val foldersWithLabels = mutableListOf<Pair<String, String>>()

        //


        // 1. Query for media files and extract folder names
        val externalUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor = context.contentResolver.query(externalUri, null, null, null, null)

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))
                val folder = path?.substringBeforeLast("/") ?: continue
                val label = commonFolderMappings[folder] ?: continue
                foldersWithLabels.add(Pair(folder, label))
            }
            cursor.close()
        }

        // 2. Leverage buckets (Android 10 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val externalVolumeNames = MediaStore.getExternalVolumeNames(context)
            for (volumeName in externalVolumeNames) {
                val volumePath = volumeName.plus("/")

                // Check Camera folder
                val cameraFolderPath = volumePath + "DCIM/Camera"
                val cameraFolderLabel = getBucketLabel(contentResolver, cameraFolderPath)
                if (cameraFolderLabel != null) {
                    foldersWithLabels.add(Pair(cameraFolderPath, cameraFolderLabel))
                }

                // Add more bucket-based queries for other folders as needed
            }
        }

        return foldersWithLabels
    }


    private fun getBucketLabel(contentResolver: ContentResolver, folderPath: String): String? {
        val volume = MediaStore.Files.getContentUri("external")
        val selection = MediaStore.Images.Media.RELATIVE_PATH + " LIKE ?"
        val selectionArgs = arrayOf(folderPath)
        val query = contentResolver.query(volume, null, selection, selectionArgs, null)

        query?.use { cursor ->
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
            }
        }

        return null
    }


}