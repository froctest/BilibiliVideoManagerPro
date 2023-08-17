package com.frstudio.bilibilivideomanagerpro.core.video

import android.media.MediaPlayer
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.frstudio.bilibilivideomanagerpro.app
import com.frstudio.bilibilivideomanagerpro.core.BDM
import com.frstudio.bilibilivideomanagerpro.core.BiliVideoProjectPage
import master.flame.danmaku.danmaku.model.android.DanmakuContext
import master.flame.danmaku.ui.widget.DanmakuView

/**
 * 可能会回调两次resultPlayer
 */
@Composable
fun DanmakuVideoPlayer(page: BiliVideoProjectPage) = DanmakuVideoPlayer(
    justVideo = page.justVideo.uri,
    justAudio = page.justAudio.uri,
    mux = page.mux?.uri,
    danmaku = page.danmakuFile.uri
)

/**
 * 可能会回调两次resultPlayer
 */
@Composable
fun DanmakuVideoPlayer(justVideo: Uri, justAudio: Uri, mux: Uri?, danmaku: Uri, error: (PlaybackException) -> Unit = {}, result: (ExoPlayer, DanmakuView) -> Unit = { _,_ -> }) {
    var isError by remember {
        mutableStateOf(false)
    }
    if (!isError && mux != null) {
        DanmakuVideoPlayer(uri = mux, danmaku = danmaku, error = {
            isError = true
        }, result = result)
    } else {
        LaunchedEffect(key1 = Unit) {
            if (isError) Toast.makeText(app, "播放出错了, 切换方案", Toast.LENGTH_SHORT).show()
        }
        DanmakuVideoPlayer(justVideo = justVideo, justAudio = justAudio, danmaku = danmaku, error = error, result = result)
    }
}
@Composable
fun DanmakuVideoPlayer(justVideo: Uri, justAudio: Uri, danmaku: Uri, error: (PlaybackException) -> Unit = {}, result: (ExoPlayer, DanmakuView) -> Unit = { _,_ -> }) {
    var musicPlayer: MediaPlayer? by remember {
        mutableStateOf(null)
    }
    MusicPlayer(justAudio) {
        musicPlayer = it
    }
    DanmakuVideoPlayer(justVideo, danmaku, start = {
        musicPlayer?.seekTo(it.toInt())
        musicPlayer?.start()
    }, pause = {
        musicPlayer?.pause()
    }, seek = {
        musicPlayer?.seekTo(it.toInt())
    }, error = {
        error(it)
    }, result)
}
@Composable
fun DanmakuVideoPlayer(
    uri: Uri,
    danmaku: Uri,
    start: (Long) -> Unit = {},
    pause: () -> Unit = {},
    seek: (Long) -> Unit = {},
    error: (PlaybackException) -> Unit = {},
    result: (ExoPlayer, DanmakuView) -> Unit = { _,_ -> }
) {
    var exoPlayer: ExoPlayer? by remember {
        mutableStateOf(null)
    }
    var danmakuView: DanmakuView? by remember {
        mutableStateOf(null)
    }
    Box(Modifier.clip(MaterialTheme.shapes.large)) {
        VideoPlayer(uri) {exo ->
            exoPlayer = exo
            exo.addListener(object : Player.Listener{
                override fun onPlayWhenReadyChanged(playWhenReady: Boolean, playbackState: Int) {
                    super.onPlayWhenReadyChanged(playWhenReady, playbackState)
                    if (playbackState == Player.PLAY_WHEN_READY_CHANGE_REASON_USER_REQUEST) {
                        if (!playWhenReady) {
                            //暂停
                            danmakuView?.pause()
                            pause()
                        } else {
                            danmakuView?.start(exo.currentPosition)
                            start(exo.currentPosition)
                        }
                    }
                }

                override fun onPositionDiscontinuity(
                    oldPosition: Player.PositionInfo,
                    newPosition: Player.PositionInfo,
                    reason: Int
                ) {
                    super.onPositionDiscontinuity(oldPosition, newPosition, reason)
                    val pos = newPosition.positionMs
                    danmakuView?.seekTo(pos)
                    seek(pos)
                }

                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    error.printStackTrace()
                    error(error)
                }

            })
        }
        Danmaku(danmaku) {
            danmakuView = it
        }
        LaunchedEffect(exoPlayer, danmakuView) {
            val exoPlayer2 = exoPlayer
            val danmakuView2 = danmakuView
            if (exoPlayer2 != null && danmakuView2 != null) result(exoPlayer2, danmakuView2)
        }
    }
}

@Composable
fun Danmaku(danmaku: Uri, resultDanmaku: (DanmakuView) -> Unit) {
    BDM(danmaku = danmaku, onPrepared = {
        //弹幕准备就绪!
    }) { dV: DanmakuView, danmakuContext: DanmakuContext ->
        resultDanmaku(dV)
//            Log.e("CC", "${dV.show()}")
    }
}