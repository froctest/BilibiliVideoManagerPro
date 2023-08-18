package com.frstudio.bilibilivideomanagerpro.ui

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.frstudio.bilibilivideomanagerpro.core.BiliVideoProjectPage
import com.frstudio.bilibilivideomanagerpro.core.getClearText
import com.frstudio.bilibilivideomanagerpro.core.prepareMuxComposable
import com.frstudio.bilibilivideomanagerpro.core.saveFile

@Composable
fun exportVideo(page: BiliVideoProjectPage): () -> Unit {
    var show by remember {
        mutableStateOf(false)
    }
    var key by remember {
        mutableStateOf(0L)
    }
    if (show) {
        prepareMuxComposable(page = page) {
            var outputUri: Uri? by remember {
                mutableStateOf(null)
            }
            outputUri?.let { output ->
                ExportDialog(it.uri, output) {
                    outputUri = null
                    show = false
                }
            }
            saveFile(defaultName = "${getClearText(page.partTitle)}.mp4", key) {
                if (it != null)outputUri = it
            }()
        }()
    }
    return {
        show = true
        key = System.nanoTime()
    }
}

@Composable
fun exportClippedVideo(page: BiliVideoProjectPage): (startPoint: Long, endPoint: Long) -> Unit {
    var show by remember {
        mutableStateOf(false)
    }
    var key by remember {
        mutableStateOf(0L)
    }
    var startPoint by remember {
        mutableStateOf(-1L)
    }
    var endPoint by remember {
        mutableStateOf(-1L)
    }
    if (show) {
        prepareMuxComposable(page = page) {
            var outputUri: Uri? by remember {
                mutableStateOf(null)
            }
            outputUri?.let { output ->
                ClipDialog(input = it.uri, output = output, startPoint = startPoint, endPoint = endPoint, autoClose = false) {
                    outputUri = null
                    show = false
                }
            }
            saveFile(defaultName = "${getClearText(page.partTitle)}.mp4", key) {
                if (it != null)outputUri = it
            }()
        }()
    }
    return { start, end ->
        startPoint = start
        endPoint = end
        show = true
        key = System.nanoTime()
    }
}

@Composable
fun exportJustAudio(page: BiliVideoProjectPage): () -> Unit {
    var show by remember {
        mutableStateOf(false)
    }
    var key by remember {
        mutableStateOf(0L)
    }
    if (show) {
        var outputUri: Uri? by remember {
            mutableStateOf(null)
        }
        outputUri?.let { output ->
            ExportDialog(page.justAudio.uri, output) {
                outputUri = null
                show = false
            }
        }
        saveFile(defaultName = "${getClearText(page.partTitle)}.mp3", key) {
            if (it != null)outputUri = it
        }()
    }
    return {
        show = true
        key = System.nanoTime()
    }
}