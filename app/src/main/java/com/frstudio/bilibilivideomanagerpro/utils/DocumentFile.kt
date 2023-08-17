package com.frstudio.bilibilivideomanagerpro.utils

import androidx.documentfile.provider.DocumentFile
import com.frstudio.bilibilivideomanagerpro.app

val DocumentFile.input get() = app.contentResolver.openInputStream(uri)
val DocumentFile.output get() = app.contentResolver.openOutputStream(uri)