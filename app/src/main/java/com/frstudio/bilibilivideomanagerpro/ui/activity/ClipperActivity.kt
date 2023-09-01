package com.frstudio.bilibilivideomanagerpro.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import androidx.media3.common.C
import androidx.media3.exoplayer.ExoPlayer
import com.frstudio.bilibilivideomanagerpro.app
import com.frstudio.bilibilivideomanagerpro.core.BiliVideoProjectPage
import com.frstudio.bilibilivideomanagerpro.core.video.DanmakuVideoPlayer
import com.frstudio.bilibilivideomanagerpro.ui.activity.ui.theme.BilibiliVideoManagerProTheme
import com.frstudio.bilibilivideomanagerpro.ui.exportClippedVideo
import com.frstudio.bilibilivideomanagerpro.utils.sec
import master.flame.danmaku.ui.widget.DanmakuView

class ClipperActivity : ComponentActivity() {

    companion object {
        /**
         * 启动VideoPlayerActivity用的
         */
        fun launch(page: BiliVideoProjectPage) {
            launch(page.pageDir.uri.toString())
        }
        /**
         * 启动VideoPlayerActivity用的
         */
        fun launch(pageDir: String) {
            val intent = Intent()
            intent.setClass(app, ClipperActivity::class.java)
                .putExtra("PageDir", pageDir)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            app.startActivity(intent)
        }
    }

    private lateinit var exoPlayer: ExoPlayer
    private lateinit var danmakuView: DanmakuView
    private var initialized: Boolean by mutableStateOf(false)
    private var startPoint by mutableStateOf(-1L)
    private var endPoint by mutableStateOf(-1L)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val page = BiliVideoProjectPage(DocumentFile.fromTreeUri(app, intent.getStringExtra("PageDir")!!.toUri())!!)
        val justVideo = page.justVideo.uri
        val justAudio = page.justAudio.uri
        val mux = page.mux?.uri
        val danmaku = page.danmakuFile?.uri
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContent {
            BilibiliVideoManagerProTheme {
                Box(Modifier.background(Color.Black)) {
                    DanmakuVideoPlayer(justVideo, justAudio, mux, danmaku = danmaku){ exo, danmaku ->
                        exoPlayer = exo
                        danmakuView = danmaku
                        initialized = true
                    }
                    if (initialized)
                        DanmakuControl(danmakuView)
                    val export = exportClippedVideo(page = page)
                    ClipperControl(
                        forward10sec = { seekToOffset(10.sec) },
                        forward1sec = { seekToOffset(1.sec) },
                        back1sec = { seekToOffset((-1).sec) },
                        startPoint = { startPoint = exoPlayer.currentPosition },
                        endPoint = { endPoint = exoPlayer.currentPosition },
                    ) {
                        //合成
                        export(startPoint, endPoint)
                    }
                }
            }
        }
    }
    @Composable
    fun BoxScope.ClipperControl(
        forward10sec: () -> Unit,
        forward1sec: () -> Unit,
        back1sec: () -> Unit,
        startPoint: () -> Unit,
        endPoint: () -> Unit,
        export: () -> Unit
    ) {
        Column(modifier = Modifier.align(Alignment.TopStart)) {
            FloatingActionButton(
                onClick = back1sec,
                modifier = Modifier.padding(12.dp, 6.dp)
            ) {
                Text(text = "1秒")
            }
            FloatingActionButton(
                onClick = startPoint,
                modifier = Modifier.padding(12.dp, 6.dp)
            ) {
                Text(text = "开头")
            }
            FloatingActionButton(
                onClick = endPoint,
                modifier = Modifier.padding(12.dp, 6.dp)
            ) {
                Text(text = "末尾")
            }
        }
        Column(modifier = Modifier.align(Alignment.TopEnd)) {
            FloatingActionButton(
                onClick = forward10sec,
                modifier = Modifier.padding(12.dp, 6.dp)
            ) {
                Text(text = "10秒")
            }
            FloatingActionButton(
                onClick = forward1sec,
                modifier = Modifier.padding(12.dp, 6.dp)
            ) {
                Text(text = "1秒")
            }
            FloatingActionButton(
                onClick = export,
                modifier = Modifier.padding(12.dp, 6.dp)
            ) {
                Text(text = "合成")
            }
        }
    }
    private fun seekToOffset(offsetMs: Long) {
        exoPlayer.seekToOffset(offsetMs)
        danmakuView.seekToOffset(offsetMs, exoPlayer.duration)
    }
}
fun DanmakuView.seekToOffset(offsetMs: Long, duration: Long) {
    var positionMs: Long = currentTime + offsetMs
    val durationMs: Long = duration
    if (durationMs != C.TIME_UNSET) {
        positionMs = positionMs.coerceAtMost(durationMs)
    }
    positionMs = positionMs.coerceAtLeast(0)
    pause()
    seekTo(positionMs)
}
