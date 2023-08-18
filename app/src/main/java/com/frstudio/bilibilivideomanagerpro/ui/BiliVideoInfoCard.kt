package com.frstudio.bilibilivideomanagerpro.ui

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.frstudio.bilibilivideomanagerpro.R
import com.frstudio.bilibilivideomanagerpro.ui.activity.VideoPlayerActivity
import com.frstudio.bilibilivideomanagerpro.app
import com.frstudio.bilibilivideomanagerpro.core.BiliVideoProject
import com.frstudio.bilibilivideomanagerpro.core.BiliVideoProjectPage
import com.frstudio.bilibilivideomanagerpro.core.video.DanmakuVideoPlayer
import com.frstudio.bilibilivideomanagerpro.core.openFileWithExternalApp
import com.frstudio.bilibilivideomanagerpro.core.prepareMux
import com.frstudio.bilibilivideomanagerpro.ui.activity.BiliVideoInfoActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BiliVideoInfoPage(
    modifier: Modifier = Modifier,
    projects: MutableList<BiliVideoProject>,
    project: BiliVideoProject,
    fix: (BiliVideoProjectPage) -> Unit,
    clicked: () -> Unit
) {
    val entry by remember {
        mutableStateOf(project.entry)
    }
    val pages = remember {
        project.pages
    }
    Card(
        modifier
            .clickable { clicked() }
            .padding(6.dp)) {
        if (pages.isNotEmpty()) {
            var currentIndex by remember {
                mutableStateOf(0)
            }
            val currentPage by remember(pages, currentIndex) {
                mutableStateOf(pages[currentIndex])
            }
            val currentMux by remember(currentPage, pages, currentIndex) {
                mutableStateOf(currentPage.mux)
            }
            val currentDanmaku by remember(currentPage, pages, currentIndex) {
                mutableStateOf(currentPage.danmakuFile)
            }
//            val currentPartTitle by remember(currentPage, pages, currentIndex) {
//                mutableStateOf(currentPage.partTitle)
//            }
            DanmakuVideoPlayer(currentPage)
            Text(text = entry.title, color = if (currentMux == null) Color.Red else Color.Unspecified)
            if (pages.size > 1) {
                LazyRow() {
                    itemsIndexed(pages) { lIndex, item ->
                        Card(onClick = {
                            currentIndex = lIndex
                        }) {
                            Box {
                                if (currentIndex == lIndex) {
                                    Text(text = item.partTitle, color = Color.Red)
                                } else Text(text = item.partTitle)
                                if (item.mux == null) GreenPoint(modifier = Modifier.size(6.dp).align(alignment = Alignment.TopEnd))
                            }
                        }
                    }
                }
            }
            Row() {
                val export = exportVideo(currentPage)
                //导出
                IconButton(onClick = { export() }, Modifier.padding(3.dp)) {
                    Icon(painter = painterResource(id = R.drawable.export), contentDescription = "导出")
                }
                IconButton(onClick = {
                    openFileWithExternalApp(currentMux!!.uri, "video/mp4", useDefault = true)
                }, Modifier.padding(3.dp)) {
                    Icon(painter = painterResource(id = R.drawable.open), contentDescription = "打开")
                }
                //其他打开方式
                IconButton(onClick = {
                    currentMux?.let {
                        openFileWithExternalApp(it.uri, "video/mp4", useDefault = false)
                    }
                }, Modifier.padding(3.dp)){
                    Icon(painter = painterResource(id = R.drawable.open), contentDescription = "")
                }
//                //修复当前Page
//                IconButton(onClick = {
//                    fix(currentPage)
//                }) {
//                    Icon(painter = painterResource(id = R.drawable.allfix), contentDescription = "")
//                }
                val shareInvoke = share(page = currentPage)
                //分享
                IconButton(onClick = {
                    shareInvoke()
                }) {
                    Icon(painter = painterResource(id = R.drawable.share), contentDescription = "")
                }
                //大屏播放
                IconButton(onClick = {
//                    VideoPlayerActivity.launch(
//                        currentPage.justVideo.uri,
//                        currentPage.justAudio.uri,
//                        currentMux?.uri,
//                        currentDanmaku.uri
//                    )
                    VideoPlayerActivity.launch(currentPage)
                }) {
                    Icon(painter = painterResource(id = R.drawable.fullscreen), contentDescription = "")
                }
                //跳转到详情页
                IconButton(onClick = {
//                    VideoPlayerActivity.launch(
//                        currentPage.justVideo.uri,
//                        currentPage.justAudio.uri,
//                        currentMux?.uri,
//                        currentDanmaku.uri
//                    )
                    BiliVideoInfoActivity.launch(currentPage)
                }) {
                    Icon(painter = painterResource(id = R.drawable.info), contentDescription = "")
                }
                //头疼砍头,因为删除功能我不会做,所以删除功能被我砍了@重生工作室
//                var delete by remember {
//                    mutableStateOf(false)
//                }
//                if (delete) Delete(projects, project, currentPage, rest = {
//                    currentIndex = 0
//                }) {
//                    delete = false
//                    project.pages.remove(currentPage)
//                }
//                IconButton(onClick = {
//                    delete = true
//                }) {
//                    Icon(painter = painterResource(id = R.drawable.delete), contentDescription = "")
//                }
                //弹幕Test
//                Button(onClick = {
//                    val intent = Intent()
//                    intent.setClass(app, AActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    intent.putExtra("Uri", projectDirUri.toString())
//                    app.startActivity(intent)
//                }) {
//                    Text(text = "弹幕Test")
//                }

            }
        }
    }
}
@Composable
fun share(page: BiliVideoProjectPage): () -> Unit {
    val invoker = prepareMux(page) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        //传输文件 采用流的方式
        shareIntent.putExtra(Intent.EXTRA_STREAM, it.uri)
        shareIntent.type = "video/mp4"
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        Log.e("GGG", "F")
        app.startActivity(shareIntent)
    }
    return {
        invoker()
    }
}
//@Composable
//fun Delete(projects: MutableList<BiliVideoProject>, project: BiliVideoProject, page: BiliVideoProjectPage, rest: () -> Unit, finish: () -> Unit) {
//    WorkDialog(work = {
//        if (project.pages.size == 1) {
//            //删除整个project
//            project.root.delete()
//            projects.remove(project)
//        } else {
//            page.pageDir.delete()
//            project.pages.remove(page)
//            rest()
//        }
//        finish()
//    }, title = "正在删除", text = {
//        LinearProgressIndicator()
//    }, confirmButton = { /*TODO*/ }) {}
//}


