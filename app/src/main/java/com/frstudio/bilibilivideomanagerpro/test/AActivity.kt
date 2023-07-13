package com.frstudio.bilibilivideomanagerpro.test

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.documentfile.provider.DocumentFile
import com.frstudio.bilibilivideomanagerpro.app
import com.frstudio.bilibilivideomanagerpro.core.getBiliVideoProject
import master.flame.danmaku.danmaku.model.BaseDanmaku
import master.flame.danmaku.danmaku.model.android.DanmakuContext
import master.flame.danmaku.ui.widget.DanmakuView
import kotlin.random.Random


class AActivity: ComponentActivity() {
    lateinit var view: DanmakuView
    lateinit var dContext: DanmakuContext

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val uri = intent.getStringExtra("Uri")!!
        val dir = DocumentFile.fromTreeUri(app, Uri.parse(uri))!!
        val project = getBiliVideoProject(dir)
        val page = project.pages[0]
        setContent {
            Column() {
                Button(onClick = {
                    for (i in 0 until Random(34274238).nextInt(2, 88)) {
                        addDanmaku()
                    }
                }) {
                    Text(text = "添加弹幕")
                }
                Button(onClick = {
                    view.seekTo(view.currentTime - 10*1000)
                }) {
                    Text(text = "时间后退10s")
                }
                Box(modifier = Modifier.fillMaxSize()) {
//                    VideoPlayer(uri = page.mux!!.uri)
//                    BDM { _view: DanmakuView, _dContext: DanmakuContext ->
//                        view = _view
//                        dContext = _dContext
//                    }
                }
            }
        }
    }

    private fun addDanmaku() {
        //创建一个弹幕对象，这里后面的属性是设置滚动方向的！
        val danmaku: BaseDanmaku =
            dContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL)
        if (!this::view.isInitialized) {
            return
        }
        //弹幕显示的文字
        danmaku.text = "这是一条弹幕" + System.nanoTime()
        //设置相应的边距，这个设置的是四周的边距
        danmaku.padding = 5
        // 可能会被各种过滤器过滤并隐藏显示，若果是本机发送的弹幕，建议设置成1；
        danmaku.priority = 1
//        //是否是直播弹幕
//        danmaku.isLive = true
        danmaku.time = view.currentTime + 100
        //设置文字大小
        danmaku.textSize = 25f
        //设置文字颜色
        danmaku.textColor = Color.RED
//        //设置阴影的颜色
//        danmaku.textShadowColor = Color.BLUE
        // danmaku.underlineColor = Color.GREEN;
//        //设置背景颜色
//        danmaku.borderColor = Color.GREEN
        Log.e("DMX", danmaku.text.toString())
        //添加这条弹幕，也就相当于发送
        view.addDanmaku(danmaku)
        Log.e("DM", danmaku.text.toString())
    }

    override fun onPause() {
        super.onPause()
        if (this::view.isInitialized && view.isPrepared) {
            view.pause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (this::view.isInitialized && view.isPrepared && view.isPaused) {
            view.resume()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::view.isInitialized) {
            // dont forget release!
            view.release()
//            view = null
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (this::view.isInitialized) {
            // dont forget release!
            view.release()
//            view = null
        }
    }
}