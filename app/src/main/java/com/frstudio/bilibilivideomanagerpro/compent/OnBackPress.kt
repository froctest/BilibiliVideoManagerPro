package com.frstudio.bilibilivideomanagerpro.compent

import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect

@Composable
fun OnBackPress(onBackPressed: () -> Unit) {
    val backDispatcher = LocalOnBackPressedDispatcherOwner.current!!.onBackPressedDispatcher

    DisposableEffect(backDispatcher) {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressed()
            }
        }

        backDispatcher.addCallback(callback)
        onDispose {
            callback.remove()
        }
    }
}