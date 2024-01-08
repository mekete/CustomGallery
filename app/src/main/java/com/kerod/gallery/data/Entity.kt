package com.kerod.gallery.data

import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.annotation.DrawableRes
import com.kerod.gallery.R



data class Media(
    val id: Long,
    val fileName: String,
    //
    val contentUri: Uri?=null,
    var favorite: Boolean = false,
    //
    var mediaType: MediaType = MediaType.IMAGE,
    val createdAt: String="",
    //
    val email: String="",
    val duration: Int=1,
    var size: Int=0,

    val bucketId: Long=1L,
    val bucketDisplayName: String="",
    val mediaFileList: List<Media> = emptyList(),
    @DrawableRes
    val avatar: Int= R.drawable.avatar_1,

) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Media) return false

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

enum class MediaType {
    IMAGE, MOVIE, SCREENSHOT, CAMERA, TRASH, FOLDER
}
