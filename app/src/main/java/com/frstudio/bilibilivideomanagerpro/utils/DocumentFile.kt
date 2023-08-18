package com.frstudio.bilibilivideomanagerpro.utils

import android.net.Uri
import androidx.documentfile.provider.DocumentFile
import com.frstudio.bilibilivideomanagerpro.app
import java.io.File

val DocumentFile.fd get() = app.contentResolver.openFileDescriptor(this.uri, "rw") !!.fileDescriptor
val DocumentFile.input get() = app.contentResolver.openInputStream(uri)!!
val DocumentFile.output get() = app.contentResolver.openOutputStream(uri)!!
val Uri.documentFile get() = DocumentFile.fromSingleUri(app ,this)
val Uri.documentFileDir get() = DocumentFile.fromTreeUri(app, this)
val Uri.isDocumentFile get() = DocumentFile.isDocumentUri(app, this)
val File.documentFile get() = DocumentFile.fromFile(this)
val DocumentFile?.exists get() = (this != null) && exists()