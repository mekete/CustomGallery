package com.kerod.gallery.data

import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.annotation.DrawableRes
import com.kerod.gallery.R


val projection = arrayOf(
    //_id, _display_name, duration, _data, _size
    MediaStore.Images.Media.BUCKET_ID,
    MediaStore.Video.Media._ID,
    MediaStore.Video.Media.DISPLAY_NAME,
    MediaStore.Video.Media.DURATION,
    MediaStore.Video.Media.DATA,
    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
    MediaStore.Images.Media.DATE_ADDED,
//    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
////    MediaStore.Images.Media.INTERNAL_CONTENT_URI,
    MediaStore.Video.Media.SIZE
)

fun aaa(cursor:Cursor){
    val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
    val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)
    val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)
    val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)
    val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
    val bucketNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
    val dateCreatedColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_ADDED)

}

data class Media(
    val id: Long=1L,
    val uid: Long= 1L,
    val fileName: String="",
    //
    val mediaUri: Uri?=null,
    var isStarred: Boolean = false,
    //
    var mediaType: MediaType = MediaType.IMAGE,
    val createdAt: String="",
    //
    val email: String="",
    val duration: Int=1,
    val size: Int=0,


    val mediaFileList: List<Media> = emptyList(),
    @DrawableRes
    val avatar: Int= R.drawable.avatar_1,
)
enum class MediaType {
    IMAGE, MOVIE, SCREENSHOT, CAMERA, TRASH, FOLDER
}
