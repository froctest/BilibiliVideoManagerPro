package com.frstudio.bilibilivideomanagerpro.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun WorkDialog(
    work: suspend () -> Unit,
    title: @Composable () -> Unit,
    text: @Composable () -> Unit,
    confirmButton: @Composable () -> Unit,
    close: () -> Unit
) {
    LaunchedEffect(Unit) {
        val job = launch {
            withContext(Dispatchers.IO) {
                work()
            }
        }
        job.join()
    }

    AlertDialog(
        onDismissRequest = close,
        title = title,
        text = text,
        confirmButton = confirmButton,
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
    )
}

@Composable
fun WorkDialog(work: suspend () -> Unit, title: String, text: @Composable () -> Unit, confirmButton: @Composable () -> Unit, close: () -> Unit) {
    WorkDialog(work, title = { Text(text = title, style = MaterialTheme.typography.bodyLarge) }, text, confirmButton, close)
}