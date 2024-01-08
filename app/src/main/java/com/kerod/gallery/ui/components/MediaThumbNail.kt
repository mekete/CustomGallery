package com.kerod.gallery.ui.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.kerod.gallery.R

@Composable
fun MediaThumbNail(
    drawableResource: Int,
    description: String,
    modifier: Modifier = Modifier
) {

    AsyncImage(
        modifier = modifier
                .size(40.dp)
                .clip(CircleShape),
        model = ImageRequest.Builder(LocalContext.current)
                .data(drawableResource)
                .placeholder(drawableResource)
                .build(),
        contentDescription = description,

    )
}


@Composable
fun SampleScreen() {
    Surface(modifier = Modifier.fillMaxSize()) {
        val context = LocalContext.current
        val placeholder = R.drawable.avatar_1
        val imageUrl = "https://image_url.jpg"

        val listener = object : ImageRequest.Listener {}



        val imageRequest = ImageRequest.Builder(context)
                .data(imageUrl)
                .listener(listener)
                //.dispatcher(Dispatcher.IO)
                .memoryCacheKey(imageUrl)
                .diskCacheKey(imageUrl)
                .placeholder(placeholder)
                .error(placeholder)
                .fallback(placeholder)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .build()

        AsyncImage(
            model = imageRequest,
            contentDescription = "Image Description",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}