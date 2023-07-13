package com.frstudio.bilibilivideomanagerpro.core

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import coil.size.Scale
import com.frstudio.bilibilivideomanagerpro.R

@Composable
fun NetworkImage(url: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
//            .size(Size.ORIGINAL) // Set the target size to load the image at.
            .scale(Scale.FIT)
            .crossfade(true)
            .error(R.drawable.ic_launcher_foreground)
            .build(),
        contentDescription = null,
        modifier = modifier
    )
}