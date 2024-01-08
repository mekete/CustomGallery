package com.kerod.gallery.data


import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.kerod.gallery.R
import java.util.Date

private const val TAG = "OrmUtl"

class OrmUtl {

    val projection = arrayOf(
        MediaStore.Images.Media._ID,
        MediaStore.Images.Media.BUCKET_ID,
        MediaStore.Images.Media.DISPLAY_NAME,
        MediaStore.Images.Media.DURATION,
        MediaStore.Images.Media.DATA,
        MediaStore.Images.Media.SIZE,
        MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Images.Media.DATE_ADDED,
        //    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        //    MediaStore.Images.Media.INTERNAL_CONTENT_URI,
    )

    fun ccc(context: Context){
        // Example: List all folders containing images
        val imageFolders: List<Pair<String, Int>> = getMediaFolders(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imageFolders.forEach { folder ->
            val folderName = folder.first
            val fileCount = folder.second
            println("Image Folder: $folderName - Files: $fileCount")
        }

        // Example: List all folders containing videos
        val videoFolders: List<Pair<String, Int>> = getMediaFolders(context, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        videoFolders.forEach { folder ->
            val folderName = folder.first
            val fileCount = folder.second
            println("Video Folder: $folderName - Files: $fileCount")
        }
    }


    private fun getMediaFolders(context: Context, contentUri: Uri): List<Pair<String, Int>> {
        val folders = mutableMapOf<String, Int>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.DURATION,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.SIZE,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,

            "COUNT(*)"
        )

        val selection = "1) GROUP BY (${MediaStore.Images.Media.BUCKET_ID}"

        context.contentResolver.query(
            contentUri,
            projection,
            selection,
            null,
            null
        )?.use { cursor ->
            val bucketIdColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
            val bucketNameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val fileCountColumn = cursor.getColumnIndexOrThrow("COUNT(*)")

            while (cursor.moveToNext()) {
                val bucketId = cursor.getString(bucketIdColumn)
                val bucketName = cursor.getString(bucketNameColumn)
                val fileCount = cursor.getInt(fileCountColumn)

                val folderPath = getFolderPath(contentUri, context.contentResolver, bucketId)
//                val thumbnail = getFolderThumbnail(context, bucketId)
                folders[folderPath] = fileCount
            }
        }

        return folders.toList()
    }

    private fun getFolderPath(contentUri: Uri, contentResolver: ContentResolver, bucketId: String): String {
        val uri = contentUri.buildUpon()
                .appendQueryParameter("distinct", "true")
                .build()

        val cursor: Cursor? = contentResolver.query(
            uri,
            arrayOf(MediaStore.Images.Media.DATA),
            "${MediaStore.Images.Media.BUCKET_ID} = ?",
            arrayOf(bucketId),
            null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)
                return it.getString(columnIndex)
            }
        }

        return ""
    }



    fun cursorToMediaList(query: Cursor?): MutableList<Media> {
        val videoList = mutableListOf<Media>()
        query?.use { cursor: Cursor ->

            val idColumn: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DURATION)
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val bucketNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val dateCreatedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED) //            val dateModifiedColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_EXPIRES)

            val folderPathSet = HashSet<String>()

            while (cursor.moveToNext()) { // Get values of columns for a given video.
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val duration = cursor.getInt(durationColumn)
                val size = cursor.getInt(sizeColumn)
                val imagePath = cursor.getString(dataColumn)
                val album = cursor.getString(bucketNameColumn)
                val created = cursor.getLong(dateCreatedColumn)
                val date = Date(created * 1000) // Convert seconds to milliseconds


                val contentUri: Uri = ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)

                // val thumbnail: Bitmap = context.contentResolver.loadThumbnail(contentUri, Size(640, 480), null)
                // Stores column values and the contentUri in a local object that represents the media file.
                videoList += Media(id = id, contentUri = contentUri, fileName = name, duration = duration, size = size, avatar = R.drawable.avatar_1)


                val folderPath = imagePath.substring(0, imagePath.lastIndexOf('/'))
                folderPathSet.add(folderPath)


                Log.e(TAG, "\n\nFile:  $name \n>>> $size \n>>> $folderPath \n$album \n>>>$date")
            }
        }
        return videoList
    }

}
