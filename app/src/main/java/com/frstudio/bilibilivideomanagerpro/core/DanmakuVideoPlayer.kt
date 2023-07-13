package com.frstudio.bilibilivideomanagerpro.core

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.documentfile.provider.DocumentFile
import com.frstudio.bilibilivideomanagerpro.test.BDM
import com.frstudio.bilibilivideomanagerpro.ui.VideoPlayer
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import master.flame.danmaku.danmaku.model.android.DanmakuContext
import master.flame.danmaku.ui.widget.DanmakuView

@Composable
fun DanmakuVideoPlayer(uri: Uri, danmaku: DocumentFile?) {
    var exoPlayer: ExoPlayer? by remember {
        mutableStateOf(null)
    }
    var danmakuView: DanmakuView? by remember {
        mutableStateOf(null)
    }
    Box(Modifier.clip(MaterialTheme.shapes.large)) {
        VideoPlayer(uri) {exo ->
            exoPlayer = exo
            exo.addListener(object :Player.Listener{
                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    super.onPlayerStateChanged(playWhenReady, playbackState)
                    if (playbackState == Player.STATE_READY) {
                        if (!playWhenReady) {
                            //暂停
                            danmakuView?.pause()
                        } else danmakuView?.start(exo.currentPosition)
                    }
                }
            })
        }
        BDM(danmaku = danmaku, onPrepared = {
            //弹幕准备就绪!
        }) { dV: DanmakuView, danmakuContext: DanmakuContext ->
            danmakuView = dV
//            Log.e("CC", "${dV.show()}")
        }
    }
}