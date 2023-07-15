package com.frstudio.bilibilivideomanagerpro.core

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.text.TextUtils
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.documentfile.provider.DocumentFile
import com.frstudio.bilibilivideomanagerpro.app

private const val defaultPath = "content://com.android.externalstorage.documents/tree/primary%3AAndroid/document/primary%3AAndroid%2Fdata%2F" + "tv.danmaku.bili" + "%2F" + "download"
private const val key = "BiliDirKey"

@Composable
fun requireStoragePermission(result: (DocumentFile?) -> Unit): ()->Unit {
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocumentTree()) {
        if (it == null) {
            result(null)
            return@rememberLauncherForActivityResult
        }
        val dir = DocumentFile.fromTreeUri(context, it)
        if (dir == null) {
            result(null)
            return@rememberLauncherForActivityResult
        }
        if (checkPath(dir)) {
            savePermission(it)
            result(dir)
            return@rememberLauncherForActivityResult
        } else {
            result(null)
            return@rememberLauncherForActivityResult
        }
    }
    return {
        getDirPermission {
            if (it == null) {
                val uri2 = Uri.parse(defaultPath)
                launcher.launch(uri2)
            } else result(DocumentFile.fromTreeUri(app, it))
        }
    }
}
//检测路径是否正确
fun checkPath(dir: DocumentFile): Boolean {
    return dir.name == "download"
}
val BiliDirSaved: Boolean get() {
    val sp: SharedPreferences = app.getSharedPreferences(key, Context.MODE_PRIVATE)
    val uriTree = sp.getString(key, "")
    return !TextUtils.isEmpty(uriTree)
}
private fun savePermission(uri: Uri) {
// 保存获取的目录权限
    val takeFlags = (Intent.FLAG_GRANT_READ_URI_PERMISSION
            or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
    app.contentResolver.takePersistableUriPermission(uri, takeFlags)
    // 保存uri
    val sp: SharedPreferences =
        app.getSharedPreferences(key, Context.MODE_PRIVATE)
    val editor = sp.edit()
    editor.putString(key, uri.toString())
    editor.apply()
}
//尝试获取保存的权限
private fun getDirPermission(result: (Uri?) -> Unit) {
    if (BiliDirSaved) {
        val sp: SharedPreferences = app.getSharedPreferences(key, Context.MODE_PRIVATE)
        val uriTree = sp.getString(key, "")
        val uri = Uri.parse(uriTree)
        val takeFlags = (Intent.FLAG_GRANT_READ_URI_PERMISSION
                or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        app.contentResolver.takePersistableUriPermission(uri, takeFlags)
        result(uri)
    } else result(null)
}