package com.frstudio.bilibilivideomanagerpro.ui

import android.net.Uri
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.frstudio.bilibilivideomanagerpro.app
import com.frstudio.bilibilivideomanagerpro.utils.getFitSize
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView

@Composable
fun VideoPlayer(uri: Uri, resultPlayer: ((ExoPlayer) -> Unit)? = null){
    val exoPlayer by remember {
        mutableStateOf(ExoPlayer.Builder(app)
            .build()
            .apply {
                playWhenReady = false
            })
    }
    //uri可以时网络url资源，这里我adb push了一个视频到使用sd卡根目录
    val mediaItem by remember(uri) {
        mutableStateOf(MediaItem.fromUri(uri))
    }
//    var savedPos by remember(uri) {
//        mutableStateOf(0L)
//    }
    LaunchedEffect(mediaItem) {
        exoPlayer.setMediaItem(mediaItem)
        exoPlayer.prepare()
//        exoPlayer.seekTo(savedPos)
    }
    DisposableEffect(Unit) {
        onDispose {
//            savedPos = exoPlayer.currentPosition
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