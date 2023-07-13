package com.frstudio.bilibilivideomanagerpro.core

import android.content.Intent
import android.net.Uri
import com.frstudio.bilibilivideomanagerpro.app

fun openFileWithExternalApp(uri: Uri, type: String, useDefault: Boolean = true) {
    val intent = Intent(Intent.ACTION_VIEW)
    intent.setDataAndType(uri, type)
    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    if (useDefault) {
        app.startActivity(intent)
    } else app.startActivity(Intent.createChooser(intent, "选择应用程序").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
//    if (intent.resolveActivity(app.packageManager) != null) {
//        app.startActivity(intent)
//    }
}