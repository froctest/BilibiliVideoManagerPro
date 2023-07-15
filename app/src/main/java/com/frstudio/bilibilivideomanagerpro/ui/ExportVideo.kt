package com.frstudio.bilibilivideomanagerpro.ui

import android.net.Uri
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.frstudio.bilibilivideomanagerpro.core.BiliVideoProject
import com.frstudio.bilibilivideomanagerpro.core.BiliVideoProjectPage
import com.frstudio.bilibilivideomanagerpro.core.getClearText
import com.frstudio.bilibilivideomanagerpro.core.muxVideoAudio
import com.frstudio.bilibilivideomanagerpro.core.saveFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun exportVideo(project: BiliVideoProject, page: BiliVideoProjectPage): () -> Unit {
    var outputUri: Uri? by remember {
        mutableStateOf(null)
    }
    var finished by remember {
        mutableStateOf(false)
    }
    LaunchedEffect(key1 = Unit) {
        withContext(Dispatchers.IO) {
            muxVideoAudio(page)
            finished = true
        }
    }
    var pick by remember {
        mutableStateOf(false)
    }
    if (pick && !finished) {
        WorkDialog(work = {}, title = "正在合成中...", text = {
            LinearProgressIndicator()
        }, confirmButton = {}) {}
    } else {
        outputUri?.let { output ->
            ExportDialog(page.mux!!.uri, output) {
                outputUri = null
            }
        }
    }
    if (pick && finished) {
        saveFile(defaultName = "${getClearText(page.partTitle)}.mp4") {
            if (it != null)outputUri = it
            pick = false
        }()
    }
    return {
        pick = true
    }
}