package com.frstudio.bilibilivideomanagerpro.core

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
fun saveFile(defaultName: String, key: Any? = null, result: (Uri?) -> Unit): @Composable ()->Unit {
    val launcher = rememberLauncherForActivityResult(
        contract = CreateDocument(
            if (defaultName.endsWith(".mp4")) "video/mp4" else if (defaultName.endsWith(".mp3")) "audio/mp3" else "*/*"
        )) {
        result(it)
    }
    return {
        LaunchedEffect(key1 = key) {
            launcher.launch(defaultName)
        }
    }
}