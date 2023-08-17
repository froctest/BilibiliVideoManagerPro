package com.frstudio.bilibilivideomanagerpro.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.frstudio.bilibilivideomanagerpro.core.danmaku.DanmakuData

@Composable
fun DanmakuViewer(danmakuData: DanmakuData, dismiss: () -> Unit) {
    Dialog(onDismissRequest = dismiss) {
        DanmakuViewerContent(danmakuData)
    }
}

@Preview
@Composable
private fun DanmakuViewerContent(danmakuData: DanmakuData = DanmakuData.DEFAULT) {
    Column() {
        Text(text = "弹幕数量: ${danmakuData.size}")
        LazyColumn {
            items(danmakuData.danmakus, key = {
                it.data+it.text
            }) {
                FloatingActionButton(onClick = {}, modifier = Modifier.padding(12.dp, 6.dp).width(1920.dp)) {
                    Text(text = it.text)
                }
            }
        }
    }
}