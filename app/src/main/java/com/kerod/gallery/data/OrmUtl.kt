package com.kerod.gallery.data;


import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import com.kerod.gallery.R
import java.util.Date

private const val TAG = "OrmUtl"

class OrmUtl {


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


                Log.e(TAG, "File:  $name \n>>> $size \n>>> $folderPath \n$album \n>>>$date")
            }
        }
        return videoList
    }

}
