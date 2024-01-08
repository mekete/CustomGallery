package com.kerod.gallery.ui.components

import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.util.Size
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.kerod.gallery.R
import com.kerod.gallery.data.Media
import com.kerod.gallery.ui.GalleryRoute

private const val TAG = "CommonFolder"

 @Composable
fun CommonFolder(
    modifier: Modifier = Modifier, currentView: String, labelResource:Int, icon:ImageVector, isSelected: Boolean = false, navigateToDetail: (Long, String,Int,String) -> Unit
) {
    Card(modifier = modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .semantics { selected = isSelected }
            .clickable { },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)) {
        Column(modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {


                Column(modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp, vertical = 4.dp), verticalArrangement = Arrangement.Center) {
                    Text(
                        text = if(GalleryRoute.IMAGE == currentView){ stringResource(id = R.string.all_image)}else{stringResource(id = R.string.all_video)},
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
                IconButton(onClick = { /*Click Implementation*/ }, modifier = Modifier.clip(CircleShape)) {
                    Icon(
                        imageVector = icon,
                        contentDescription = stringResource(labelResource),
                    )
                }
            }
        }
    }
}

