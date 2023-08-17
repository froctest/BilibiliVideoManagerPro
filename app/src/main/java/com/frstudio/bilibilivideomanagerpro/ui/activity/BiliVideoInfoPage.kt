package com.frstudio.bilibilivideomanagerpro.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import com.frstudio.bilibilivideomanagerpro.R
import com.frstudio.bilibilivideomanagerpro.app
import com.frstudio.bilibilivideomanagerpro.core.BiliVideoProjectPage
import com.frstudio.bilibilivideomanagerpro.core.danmaku.DanmakuData
import com.frstudio.bilibilivideomanagerpro.ui.DanmakuViewer
import com.frstudio.bilibilivideomanagerpro.ui.exportJustAudio

class BiliVideoInfoActivity: ComponentActivity() {
    companion object{
        fun launch(page: BiliVideoProjectPage) {
            val intent = Intent()
            intent.setClass(app, BiliVideoInfoActivity::class.java)
                .putExtra("PageDir", page.pageDir.uri.toString())
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            app.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pageDir = DocumentFile.fromTreeUri(app, intent.getStringExtra("PageDir")!!.toUri())!!
        val page = BiliVideoProjectPage(pageDir)
        setContent {
            BiliVideoInfoPage(page = page)
        }
    }
}

@Composable
fun BiliVideoInfoPage(page: BiliVideoProjectPage) {
    val danmaku by remember {
        mutableStateOf(DanmakuData(page.danmakuFile))
    }
    val export = exportJustAudio(page)
    var showDanmakuDialog by remember(page) {
        mutableStateOf(false)
    }
    if (showDanmakuDialog) DanmakuViewer(DanmakuData(page.danmakuFile)) {
        showDanmakuDialog = false
    }
    BiliVideoInfoPage(
        title = page.title,
        danmakuCount = danmaku.size,
        intentPlayVideoActivity = { VideoPlayerActivity.launch(page) },
        showDanmaku = {
            showDanmakuDialog = true
        },
        dingzhengMode = {
            DingzhenActivity.launch(page.justVideo.uri)
        },
        videoSplit = {},
        audioExport = {
            export()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun BiliVideoInfoPage(
    title: String = "好像是标题",
    danmakuCount: Int = 114514,
    intentPlayVideoActivity: () -> Unit = {},
    showDanmaku: () -> Unit = {  },
    dingzhengMode: () -> Unit = {},
    videoSplit: () -> Unit = {},
    audioExport: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = title)
            })
        }, floatingActionButton = {
            FloatingActionButton(onClick = {
                intentPlayVideoActivity()
            }, modifier = Modifier.size(60.dp)) {
                Icon(painter = painterResource(id = R.drawable.fullscreen), contentDescription = "")
            }
        }
    ) { contentPadding ->
        // Screen content
        Column(modifier = Modifier.padding(contentPadding)) {
            //弹幕数量
            FloatingActionButton(onClick = {
                showDanmaku()
            }, modifier = Modifier
                .padding(12.dp, 4.dp)
                .fillMaxWidth()) {
                Text(text = "弹幕数量: $danmakuCount")
            }
            FloatingActionButton(onClick = {
                dingzhengMode()
            }, modifier = Modifier
                .padding(12.dp, 4.dp)
                .fillMaxWidth()) {
                Text(text = "盯帧模式")
            }
            FloatingActionButton(onClick = {
                videoSplit()
            }, modifier = Modifier
                .padding(12.dp, 4.dp)
                .fillMaxWidth()) {
                Text(text = "片段分割")
            }
            FloatingActionButton(onClick = {
                audioExport()
            }, modifier = Modifier
                .padding(12.dp, 4.dp)
                .fillMaxWidth()) {
                Text(text = "音频提取")
            }
        }
    }
}