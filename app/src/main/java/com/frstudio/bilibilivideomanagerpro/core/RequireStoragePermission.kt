package com.frstudio.bilibilivideomanagerpro.core

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.documentfile.provider.DocumentFile

@Composable
fun requireStoragePermission(result: (DocumentFile?) -> Unit): ()->Unit {
    //申请Android/data/B站/download文件夹访问权限
//    val uri1 = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata");
//    val intent1 = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
//    intent1.flags = (Intent.FLAG_GRANT_READ_URI_PERMISSION
//            or  Intent.FLAG_GRANT_WRITE_URI_PERMISSION
//            or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
//            or Intent.FLAG_GRANT_PREFIX_URI_PERMISSION)
//    intent1.putExtra(DocumentsContract.EXTRA_INITIAL_URI, uri1);
////    startActivityForResult(intent1, 11);
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.OpenDocumentTree()) {
        if (it == null) {
            result(null)
            return@rememberLauncherForActivityResult
        }
        biliFolderUri = it
        val dir = DocumentFile.fromTreeUri(context, it)
        if (dir!!.name != "download") {
            result(null)
            return@rememberLauncherForActivityResult
        }
        result(dir)
    }
    return {
        val uri2 = Uri.parse("content://com.android.externalstorage.documents/tree/primary%3AAndroid/document/primary%3AAndroid%2Fdata%2F" + "tv.danmaku.bili" + "%2F" + "download")
        launcher.launch(uri2)
    }
}