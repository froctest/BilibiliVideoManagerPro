package com.frstudio.bilibilivideomanagerpro.ui

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.frstudio.bilibilivideomanagerpro.R
import com.frstudio.bilibilivideomanagerpro.core.BiliVideoProject
import com.frstudio.bilibilivideomanagerpro.core.DanmakuVideoPlayer
import com.frstudio.bilibilivideomanagerpro.core.getClearText
import com.frstudio.bilibilivideomanagerpro.core.openFileWithExternalApp
import com.frstudio.bilibilivideomanagerpro.core.saveFile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BiliVideoInfoPage(
    modifier: Modifier = Modifier,
    project: BiliVideoProject,
    fix: (Int) -> Unit,
    clicked: () -> Unit
) {
    val entry by remember {
        mutableStateOf(project.entry)
    }
    val pages by remember {
        mutableStateOf(project.pages)
    }
    Card(
        modifier
            .clickable { clicked() }
            .padding(6.dp)) {
        if (pages.isNotEmpty()) {
            var currentIndex by remember {
                mutableStateOf(0)
            }
            var currentPage by remember(pages, currentIndex) {
                mutableStateOf(pages[currentIndex])
            }
            var currentMux by remember(pages, currentIndex) {
                mutableStateOf(currentPage.mux)
            }
            val currentDanmaku by remember(pages, currentIndex) {
                mutableStateOf(currentPage.danmakuFile)
            }
            val currentPartTitle by remember(pages, currentIndex) {
                mutableStateOf(currentPage.pageData.part?:entry.title)
            }
            DanmakuVideoPlayer(currentMux!!.uri, danmaku = currentDanmaku)
            Text(text = entry.title)
            if (pages.size > 1) {
                LazyRow() {
                    itemsIndexed(pages) { lIndex, item ->
                        Card(onClick = {
                            currentIndex = lIndex
                        }) {
                            if (currentIndex == lIndex) {
                                Text(text = item.partTitle, color = Color.Red)
                            } else Text(text = item.partTitle)
                        }
                    }
                }
            }
            Row() {
                var outputUri: Uri? by remember {
                    mutableStateOf(null)
                }
                val export = saveFile(defaultName = "${getClearText(currentPartTitle)}.mp4") {
                    if (it != null)outputUri = it
                }
                outputUri?.let { output ->
                    ExportDialog(currentMux!!.uri, output) { outputUri = null }
                }
                IconButton(onClick = { export() }, Modifier.padding(3.dp)) {
                    Icon(painter = painterResource(id = R.drawable.export), contentDescription = "导出")
                }
                IconButton(onClick = {
                    openFileWithExternalApp(currentMux!!.uri, "video/mp4", useDefault = true)
                }, Modifier.padding(3.dp)) {
                    Icon(painter = painterResource(id = R.drawable.open), contentDescription = "打开")
                }
                IconButton(onClick = {
                    fix(currentIndex)
                }) {
                    Icon(painter = painterResource(id = R.drawable.allfix), contentDescription = "")
                }
//                Button(onClick = {
//                    val intent = Intent()
//                    intent.setClass(app, AActivity::class.java)
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    intent.putExtra("Uri", projectDirUri.toString())
//                    app.startActivity(intent)
//                }) {
//                    Text(text = "弹幕Test")
//                }
//                Button(onClick = {
//                    openFileWithExternalApp(mux, "video/mp4", useDefault = false)
//                }, Modifier.padding(3.dp)){
//                    Text(text = "其他打开方式")
//                }
            }
        }
    }
}

