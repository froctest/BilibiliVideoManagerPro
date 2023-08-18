package com.frstudio.bilibilivideomanagerpro.utils

import android.net.Uri
import java.io.File

/**
 * 仅支持文件,不支持文件夹
 */
fun tempFile(uri: Uri): File {
    if (uri.isDocumentFile) {
        val doc = uri.documentFile!!
        val temp = File.createTempFile("temp", ".temp")
        doc.input.transfer(temp.outputStream())
        return temp
    }
    throw UnsupportedOperationException("仅支持文件,不支持文件夹")
}