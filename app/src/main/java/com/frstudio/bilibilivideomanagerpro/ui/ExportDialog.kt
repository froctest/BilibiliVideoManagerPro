package com.frstudio.bilibilivideomanagerpro.ui

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import com.frstudio.bilibilivideomanagerpro.app
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ExportDialog(input: Uri, output: Uri, close: () -> Unit) {
    var progress by remember { mutableStateOf(0.0f) }
    val totalBytes by remember {
        mutableStateOf(DocumentFile.fromSingleUri(app, input)!!.length())
    }
    var copiedBytes by remember {
        mutableStateOf(0L)
    }
    WorkDialog(
        work = {
            try {
                withContext(Dispatchers.IO) {
                    val inputStream = app.contentResolver.openInputStream(input)
                    val outputStream = app.contentResolver.openOutputStream(output)

                    if (inputStream != null && outputStream != null) {

                        val buffer = ByteArray(4096)
                        var bytesRead = inputStream.read(buffer)

                        while (bytesRead >= 0) {
                            outputStream.write(buffer, 0, bytesRead)
                            copiedBytes += bytesRead

                            progress = (copiedBytes.toFloat() / totalBytes) * 100

                            bytesRead = inputStream.read(buffer)
                        }

                        inputStream.close()
                        outputStream.close()
                    }
                }
            } catch (e: Exception) {
                // 处理异常情况
            }
        },
        title = (if (totalBytes == copiedBytes) "完成!" else "导出中..."),
        text = {
            if (totalBytes != copiedBytes) {
                Column(Modifier.fillMaxWidth()) {
                    LinearProgressIndicator(progress / 100)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("导出进度：${progress.toInt()}%")
                }
            } else {
                Text("复制完成！")
            }
        },
        confirmButton = {
            if (totalBytes == copiedBytes) {
                Button(onClick = close) {
                    Text("关闭")
                }
            }
        }, close)
}
//    var progress by remember { mutableStateOf(0.0f) }
//    val totalBytes by remember {
//        mutableStateOf(DocumentFile.fromSingleUri(app, input)!!.length())
//    }
//    var copiedBytes by remember {
//        mutableStateOf(0L)
//    }
//    LaunchedEffect(Unit) {
//        val copyJob = launch {
//            try {
//                withContext(Dispatchers.IO) {
//                    val inputStream = app.contentResolver.openInputStream(input)
//                    val outputStream = app.contentResolver.openOutputStream(output)
//
//                    if (inputStream != null && outputStream != null) {
//
//                        val buffer = ByteArray(4096)
//                        var bytesRead = inputStream.read(buffer)
//
//                        while (bytesRead >= 0) {
//                            outputStream.write(buffer, 0, bytesRead)
//                            copiedBytes += bytesRead
//
//                            progress = (copiedBytes.toFloat() / totalBytes) * 100
//
//                            bytesRead = inputStream.read(buffer)
//                        }
//
//                        inputStream.close()
//                        outputStream.close()
//                    }
//                }
//            } catch (e: Exception) {
//                // 处理异常情况
//            }
//        }
//
//        copyJob.join()
//    }
//
//    AlertDialog(
//        onDismissRequest = close,
//        title = { Text(if (totalBytes == copiedBytes) "完成!" else "导出中...") },
//        text = {
//            if (totalBytes != copiedBytes) {
//                Column(Modifier.fillMaxWidth()) {
//                    LinearProgressIndicator(progress / 100)
//                    Spacer(modifier = Modifier.height(8.dp))
//                    Text("导出进度：${progress.toInt()}%")
//                }
//            } else {
//                Text("复制完成！")
//            }
//        },
//        confirmButton = {
//            if (totalBytes == copiedBytes) {
//                Button(onClick = close) {
//                    Text("关闭")
//                }
//            }
//        }
//    )
//}