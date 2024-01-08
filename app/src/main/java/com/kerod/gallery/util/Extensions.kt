package com.kerod.gallery.util
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.Settings
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class Extensions {

    private fun String?.trimSafely(): String = this?.trim() ?: ""
    fun String?.limitLength(length:Int): String = if (this?.trimSafely()?.length!! >  length){this.trimSafely().substring(length)}else{this.trimSafely()}





    fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        var inputStream: InputStream? = null
        try {
            val contentResolver: ContentResolver = context.contentResolver
            inputStream = contentResolver.openInputStream(uri)
            if (inputStream != null) {
                return BitmapFactory.decodeStream(inputStream)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
        }
        return null
    }
}
fun Date.toFormattedDateString(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
    //        val date = Date(this)
    return dateFormat.format(this)
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