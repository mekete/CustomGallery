/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
    //imageUri: Uri,
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
        val placeholder = R.drawable.avatar_0
        val imageUrl = "https://image_url.jpg"

        val listener = object : ImageRequest.Listener {
            override fun onError(request: ImageRequest, result: ErrorResult) {
                super.onError(request, result)
            }
            override fun onSuccess(request: ImageRequest, result: SuccessResult) {
                super.onSuccess(request, result)
            }
        }



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
        // Load and display the image with AsyncImage
        AsyncImage(
            model = imageRequest,
            contentDescription = "Image Description",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}