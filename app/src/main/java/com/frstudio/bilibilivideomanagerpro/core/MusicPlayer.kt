package com.frstudio.bilibilivideomanagerpro.core

import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.frstudio.bilibilivideomanagerpro.app
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun MusicPlayer(uri: Uri, prepared: (MediaPlayer) -> Unit) {
    val mediaPlayer: MediaPlayer by remember {
        mutableStateOf(MediaPlayer())
    }
    LaunchedEffect(key1 = Unit) {
        withContext(Dispatchers.IO) {
            mediaPlayer.setDataSource(app, uri)
            mediaPlayer.prepare()// Error java.lang.IllegalStateException TODO 解决
            mediaPlayer.setOnPreparedListener(prepared)
        }
    }
    DisposableEffect(key1 = Unit) {
        onDispose {
            mediaPlayer.release()
        }
    }
}