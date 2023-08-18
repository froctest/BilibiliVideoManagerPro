package com.frstudio.bilibilivideomanagerpro.ui.activity


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.core.view.isVisible
import androidx.media3.exoplayer.ExoPlayer
import com.frstudio.bilibilivideomanagerpro.R
import com.frstudio.bilibilivideomanagerpro.app
import com.frstudio.bilibilivideomanagerpro.core.BiliVideoProjectPage
import com.frstudio.bilibilivideomanagerpro.core.video.DanmakuVideoPlayer
import com.frstudio.bilibilivideomanagerpro.ui.theme.Blue
import master.flame.danmaku.ui.widget.DanmakuView


class VideoPlayerActivity: ComponentActivity() {
    companion object{
        /**
         * 启动VideoPlayerActivity用的
         */
        fun launch(page: BiliVideoProjectPage) {
            launch(page.justVideo.uri, page.justAudio.uri, page.mux!!.uri, danmaku = page.danmakuFile.uri)
        }
        /**
         * 启动VideoPlayerActivity用的
         */
        fun launch(justVideo: Uri, justAudio: Uri, mux: Uri? = null, danmaku: Uri) {
            val intent = Intent()
            intent.setClass(app, VideoPlayerActivity::class.java)
                .putExtra("justVideo", justVideo.toString())
                .putExtra("justAudio", justAudio.toString())
                .putExtra("mux", mux?.toString())
                .putExtra("danmaku", danmaku.toString())
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            app.startActivity(intent)
        }
    }
    private lateinit var justVideo: Uri
    private lateinit var justAudio: Uri
    private var mux: Uri? = null
    private lateinit var danmaku: Uri

    private lateinit var exoPlayer: ExoPlayer
    private lateinit var danmakuView: DanmakuView
    private var initialized: Boolean by mutableStateOf(false)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        justVideo = Uri.parse(intent.getStringExtra("justVideo"))
        justAudio = Uri.parse(intent.getStringExtra("justAudio"))
        mux = try {
            Uri.parse(intent.getStringExtra("mux"))
        } catch (e: Exception) {
            null
        }
        danmaku = Uri.parse(intent.getStringExtra("danmaku"))
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContent {
            MaterialTheme() {
                Box(Modifier.background(Color.Black)) {
                    DanmakuVideoPlayer(justVideo, justAudio, mux, danmaku = danmaku){ exo, danmaku ->
                        exoPlayer = exo
                        danmakuView = danmaku
                        initialized = true
                    }
                    if (initialized)
                        DanmakuControl(danmakuView)
                }
            }
        }
        val flagsFullScreen = WindowManager.LayoutParams.FLAG_FULLSCREEN
        window.addFlags(flagsFullScreen); // 设置全屏
    }
}
@Composable
fun DanmakuControl(danmakuView: DanmakuView) {
    Column(Modifier.fillMaxHeight()) {
        var visibility by remember {
            mutableStateOf(danmakuView.isVisible)
        }
        IconButton(onClick = {
            danmakuView.visibility = if (visibility) View.INVISIBLE else View.VISIBLE
            visibility = danmakuView.isVisible
        }) {
            Icon(painter = painterResource(id = if (visibility) R.drawable.danmaku_off else R.drawable.danmaku_on), contentDescription = "", tint = Blue)
        }
    }
}