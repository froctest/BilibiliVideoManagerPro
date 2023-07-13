package com.frstudio.bilibilivideomanagerpro.core

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.compose.runtime.Composable

@Composable
fun saveFile(defaultName: String, result: (Uri?) -> Unit): ()->Unit {
    val launcher = rememberLauncherForActivityResult(contract = CreateDocument("video/mp4")) {
        result(it)
    }
    return {
        launcher.launch(defaultName)
    }
}