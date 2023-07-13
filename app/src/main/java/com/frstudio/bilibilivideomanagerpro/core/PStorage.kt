package com.frstudio.bilibilivideomanagerpro.core

import android.net.Uri
import androidx.core.content.edit
import com.frstudio.bilibilivideomanagerpro.sharedPreferences

var biliFolderUri: Uri? get() {
    return sharedPreferences.getString("biliFolderUri", null)?.let {
        val uri = Uri.parse(it)
//        val takeFlag = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
//        app.contentResolver.takePersistableUriPermission(uri, takeFlag)
        uri
    }
} set(value) {
    sharedPreferences.edit {
        putString("biliFolderUri", value.toString())
    }
}