package com.frstudio.bilibilivideomanagerpro.utils

import androidx.compose.ui.unit.Dp
import androidx.documentfile.provider.DocumentFile
import com.frstudio.bilibilivideomanagerpro.app

val DocumentFile.fd get() = app.contentResolver.openFileDescriptor(this.uri, "rw") !!.fileDescriptor
fun getFitSize(length: Dp, percentLength: Int, percentOut: Int):Dp {
    val percent = length / percentLength
    return percent * percentOut
}