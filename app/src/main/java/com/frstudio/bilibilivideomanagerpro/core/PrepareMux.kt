package com.frstudio.bilibilivideomanagerpro.core

import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.documentfile.provider.DocumentFile
import com.frstudio.bilibilivideomanagerpro.ui.WorkDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun prepareMuxComposable(page: BiliVideoProjectPage, prepared: @Composable (DocumentFile) -> Unit): () -> Unit {
    var finished by remember(page) {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = page) {
        withContext(Dispatchers.IO) {
            muxVideoAudio(page)
            finished = true
        }
    }
    var start by remember(page) {
        mutableStateOf(false)
    }
    if (start && !finished) {
        WorkDialog(work = {}, title = "正在合成中...", text = {
            LinearProgressIndicator()
        }, confirmButton = {}) {}
    } else if (start) {
        val mux = page.mux!!
        prepared(mux)
        start = false
    }
    return {
        start = true
    }
}

@Composable
fun prepareMux(page: BiliVideoProjectPage, prepared: (DocumentFile) -> Unit): () -> Unit {
    return prepareMuxComposable(page = page) {
        LaunchedEffect(key1 = page) {
            prepared(it)
        }
    }
}