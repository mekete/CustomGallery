package com.kerod.gallery

import android.app.Application
import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kerod.gallery.data.Media
import com.kerod.gallery.ui.GalleryRoute
import com.kerod.gallery.util.toFormattedDateString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.Date

private const val TAG = "FolderListViewMode"

class FolderListViewModel(application: Application) : AndroidViewModel(application) {

    private val contentResolver: ContentResolver = application.contentResolver

    private val _imageFolders: MutableStateFlow<List<Media>> = MutableStateFlow(emptyList())
    private val _videoFolders: MutableStateFlow<List<Media>> = MutableStateFlow(emptyList())
    private val _filesInFolder: MutableStateFlow<List<Media>> = MutableStateFlow(emptyList())

    //
    val imageFolders: StateFlow<List<Media>> = _imageFolders.asStateFlow()
    val videoFolders: StateFlow<List<Media>> = _videoFolders.asStateFlow()
    val filesInFolder: StateFlow<List<Media>> = _filesInFolder.asStateFlow()
 
    init {
        initLoadingFolder()
    }

    private val projection = arrayOf(
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.BUCKET_ID,
        MediaStore.Video.Media.DISPLAY_NAME,
        MediaStore.Video.Media.DURATION,
        MediaStore.Video.Media.DATA,
        MediaStore.Video.Media.IS_FAVORITE,
        MediaStore.Video.Media.MIME_TYPE,
        MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
        MediaStore.Video.Media.DATE_ADDED,
        MediaStore.Video.Media.SIZE,
    )

    private fun initLoadingFolder() {
        viewModelScope.launch {

            loadFoldersContainingMedia(contentResolver, GalleryRoute.IMAGE).collect{
                _imageFolders.value += it
            }
            loadFoldersContainingMedia(contentResolver, GalleryRoute.MOVIE).collect{
                _videoFolders.value += it
            }
        }
     }
    private fun initLoadingFilesInFolder(typeOfFolder: String = GalleryRoute.IMAGE, bucketId: Long, bucketName: String, size:Int) {
        viewModelScope.launch {
            loadMediasInsideFolder(typeOfFolder  = typeOfFolder, bucketId=bucketId, bucketName=bucketName , size=size).collect{
                 _imageFolders.value += it
            } 
        }
    }

    private fun loadMediasInsideFolder(typeOfFolder: String = GalleryRoute.IMAGE, bucketId: Long, bucketName: String, size:Int): Flow<Media> = flow {

        val externalUri = getExternalMediaUri(typeOfFolder)
        val selection = "${MediaStore.Video.Media.BUCKET_DISPLAY_NAME} = ?"//"${MediaStore.Video.Media.BUCKET_DISPLAY_NAME} = ?"
        val selectionArgs = arrayOf( bucketName) //arrayOf("DD_FILLED") //null //arrayOf(  TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES).toString())
        val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"
        val cursor = contentResolver.query(externalUri, projection, selection, selectionArgs, sortOrder) ?: return@flow

        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
        val mimeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)
        val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DURATION)
        val favoriteColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.IS_FAVORITE)
        val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE) //        val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        val bucketNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val dateCreatedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED) //



        cursor.use { row ->
            if (row.moveToFirst()) {
                do {
                    val bucketId = row.getLong(row.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID))
                    val bucketDisplayName = row.getString(row.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))


                    val id = row.getLong(idColumn)
                    val fileName = row.getString(nameColumn)
                    val duration = row.getInt(durationColumn)
                    val size = row.getInt(sizeColumn)
                    val mime = row.getInt(mimeColumn)
                    val album = row.getString(bucketNameColumn)
                    val favorite = row.getString(favoriteColumn)
                    val created = row.getLong(dateCreatedColumn)
                    val date = Date(created * 1000).toString() // Convert seconds to milliseconds


                    val contentUri: Uri = ContentUris.withAppendedId(externalUri, id)

                    if (bucketDisplayName == null) {
                        continue
                    }
                    val media = Media(id = id, contentUri = contentUri, bucketId = bucketId, bucketDisplayName = bucketDisplayName, fileName = fileName, duration = duration, size = size, createdAt = date , avatar = R.drawable.avatar_1)

                    emit(media)
                } while (row.moveToNext())
            }
        }
    }.flowOn(Dispatchers.IO)

    private fun loadFoldersContainingMedia(contentResolver: ContentResolver, typeOfFolder: String): Flow<Media> = flow {

        val externalUri = getExternalMediaUri(typeOfFolder)
        val selection = null //"${MediaStore.Video.Media.DURATION} >= ?"
        val selectionArgs = null //arrayOf(  TimeUnit.MILLISECONDS.convert(5, TimeUnit.MINUTES).toString())
        val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"
        val cursor = contentResolver.query(externalUri, projection, selection, selectionArgs, sortOrder) ?: return@flow

        val idIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        val pathColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        val bucketIdIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
        val folderColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
        val dateCreatedColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED) //

        cursor.use { row ->

            val folders = mutableListOf<Media>()
            if (row.moveToFirst()) {

                do {
                    val id = row.getLong(idIndex)
                    val imagePath = row.getString(pathColumnIndex)
                    val bucketId = row.getLong(bucketIdIndex)
                    val bucketDisplayName = row.getString(folderColumnIndex) ?: continue
                    val createdAt=  Date(row.getLong(dateCreatedColumn) * 1000).toFormattedDateString()

                    val contentUri: Uri = ContentUris.withAppendedId(externalUri, id)
                    val folder = Media(id = id, bucketId = bucketId, createdAt = createdAt, bucketDisplayName = bucketDisplayName, fileName = imagePath, size = 1, contentUri = contentUri)
                    val previouslyAdded = folders.find { it.bucketId == folder.bucketId }


                    if (previouslyAdded != null) {
                        Log.e(TAG, "\n\n>>>>>>>IIIII ${folder.size} ${folder.bucketDisplayName}: ${folder.fileName} ")
                        previouslyAdded.size += 1
                    } else {
                        Log.e(TAG, "\n\n>>>>>>>JJJJ ${folder.size} ${folder.bucketDisplayName}: ${folder.fileName} ")
                        folders.add(folder)
                    }

                } while (row.moveToNext())
            }

            folders.forEach { folder ->
                Log.e(TAG, "\n\n>>>>>>>KKKKK ${folder.size} ${folder.bucketDisplayName}: ${folder.fileName} ")
                emit(folder)
            }
        }

    }.flowOn(Dispatchers.IO)

    private fun getExternalMediaUri(typeOfFolder: String): Uri {
        return if (typeOfFolder == GalleryRoute.IMAGE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else {
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else {
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            }
        }
    }

}