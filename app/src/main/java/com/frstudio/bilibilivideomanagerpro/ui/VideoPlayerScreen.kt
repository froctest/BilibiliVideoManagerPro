package com.frstudio.bilibilivideomanagerpro.ui

import android.net.Uri
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackParameters
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.frstudio.bilibilivideomanagerpro.app
import com.frstudio.bilibilivideomanagerpro.utils.getFitSize

@Composable
fun VideoPlayer(uri: Uri, resultPlayer: ((ExoPlayer) -> Unit)? = null){
    val exoPlayer by remember {
        mutableStateOf(ExoPlayer.Builder(app)
            .build()
            .apply {
                playWhenReady = false
                playbackParameters = PlaybackParameters.DEFAULT
                setPlaybackSpeed(1.0F)
            })
    }
    val mediaItem: MediaItem by remember(uri) {
        mutableStateOf(MediaItem.fromUri(uri))
    }
//    val savePos: Long by remember(uri) {
//        mutableStateOf(uri.getSave("savePos") { 0L })
//    }
    LaunchedEffect(uri) {
//        try {
//            exoPlayer.release()
//        } catch (_: Exception) {}
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
//        if (savePos != 0L) {
//            exoPlayer.seekTo(savePos)
//            exoPlayer.play()
//        }
    }
    DisposableEffect(Unit) {
        onDispose {
//            uri.saveStorage["mediaItem"] = mediaItem
//            uri.saveStorage["savePos"] = exoPlayer.currentPosition
            exoPlayer.release()
            //TODO 好像要处理野指针
        }
    }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val height = getFitSize(screenWidth, 1920, 1080)
    PlayerSurface(modifier = Modifier.size(screenWidth, height)){
        it.player = exoPlayer
    }
    if (resultPlayer != null) {
        resultPlayer(exoPlayer)
    }
}

@Composable
fun PlayerSurface(
    modifier: Modifier = Modifier,
    onPlayerViewAvailable: (PlayerView) -> Unit = {}
) {
    AndroidView(
        factory = { context ->
            PlayerView(context).apply {
                useController = true
                onPlayerViewAvailable(this)
            }
        },
        modifier = modifier
    )
}