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