package com.kerod.gallery.ui.components

import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kerod.gallery.R
import com.kerod.gallery.data.Media

private const val TAG = "MediaItem"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MediaItem(
    album: Media,
    modifier: Modifier = Modifier
) {

    Card(modifier = modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            //.semantics { selected = isSelected }
            .clickable {
                //navigateToDetail(album.bucketId, album.bucketDisplayName, album.size, currentView)
            }, colors = CardDefaults.cardColors(containerColor = if (album.favorite) MaterialTheme.colorScheme.secondaryContainer
    else MaterialTheme.colorScheme.surfaceVariant)) {
        Column(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)) {
            if (album.contentUri != null) {


                val thumbnail: Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    LocalContext.current.contentResolver.loadThumbnail(album.contentUri, Size(144, 144), null)
                } else {
                    MediaStore.Images.Media.getBitmap(LocalContext.current.contentResolver, album.contentUri)
                }
                Row(modifier = Modifier.fillMaxWidth()) {
                    AsyncImage(modifier = modifier.size(96.dp), model = ImageRequest.Builder(LocalContext.current).data(thumbnail).placeholder(album.avatar).build(), contentDescription = album.fileName, contentScale = ContentScale.Crop)

                    Column(modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 12.dp, vertical = 4.dp), verticalArrangement = Arrangement.Center) {

                        Text(text = album.bucketDisplayName, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            text = "\nFiles: ${album.size}\nCreated:${album.createdAt}",
                            style = MaterialTheme.typography.labelMedium,
                        )
                        Button(
                            onClick = { /*Click Implementation*/ },
                            modifier = Modifier.weight(1f),
                        ) {
                            Text(
                                text = stringResource(id = R.string.gallery),
                            )
                        }
                    }
                    IconButton(onClick = { /*Click Implementation*/ }, modifier = Modifier.clip(CircleShape)) {
                        Icon(
                            imageVector = Icons.Default.StarBorder,
                            contentDescription = "Favorite",
                        )
                    }
                }
            } else {
                Log.e(TAG, "\n\n\nFolderItem::::\n\n\n${album.fileName} >>>>")

            }
        }
    }



}
