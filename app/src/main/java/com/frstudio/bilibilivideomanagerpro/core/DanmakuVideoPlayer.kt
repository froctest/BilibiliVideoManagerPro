package com.frstudio.bilibilivideomanagerpro.core

import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.documentfile.provider.DocumentFile
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.frstudio.bilibilivideomanagerpro.test.BDM
import com.frstudio.bilibilivideomanagerpro.ui.VideoPlayer
import master.flame.danmaku.danmaku.model.android.DanmakuContext
import master.flame.danmaku.ui.widget.DanmakuView

@Composable
fun DanmakuVideoPlayer(justVideo: Uri, justAudio: Uri, danmaku: DocumentFile?) {
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
    })
}
@Composable
fun DanmakuVideoPlayer(uri: Uri, danmaku: DocumentFile?, start: (Long) -> Unit = {}, pause: () -> Unit = {}, seek: (Long) -> Unit = {}, resultPlayer: (ExoPlayer) -> Unit = {}) {
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

            })
        }
        Danmaku(danmaku) {
            danmakuView = it
        }
    }
}

@Composable
fun Danmaku(danmaku: DocumentFile?, resultDanmaku: (DanmakuView) -> Unit) {
    BDM(danmaku = danmaku, onPrepared = {
        //弹幕准备就绪!
    }) { dV: DanmakuView, danmakuContext: DanmakuContext ->
        resultDanmaku(dV)
//            Log.e("CC", "${dV.show()}")
    }
}