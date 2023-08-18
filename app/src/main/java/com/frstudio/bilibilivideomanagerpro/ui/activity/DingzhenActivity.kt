package com.frstudio.bilibilivideomanagerpro.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.FloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.media3.common.C
import androidx.media3.exoplayer.ExoPlayer
import com.frstudio.bilibilivideomanagerpro.app
import com.frstudio.bilibilivideomanagerpro.core.video.VideoPlayer
import com.frstudio.bilibilivideomanagerpro.utils.OneTenthSec
import com.frstudio.bilibilivideomanagerpro.utils.sec

class DingzhenActivity: ComponentActivity() {

    companion object {
        fun launch(videoUri: Uri) {
            val intent = Intent()
            intent.setClass(app, DingzhenActivity::class.java)
                .putExtra("videoUri", videoUri.toString())
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            app.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val videoUri = intent.getStringExtra("videoUri")!!.toUri()
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContent {
            DingzhenPage(videoUri = videoUri)
        }
    }
}

@Composable
fun DingzhenPage(videoUri: Uri) {
    var posText by remember {
        mutableStateOf("--:--")
    }
    var player: ExoPlayer? by remember {
        mutableStateOf(null)
    }
    DingzhenPage(
        forward10sec = { player?.seekToOffset(10.sec) },
        forward1sec = { player?.seekToOffset(1.sec) },
        forwardOneTenthSec = { player?.seekToOffset(1.OneTenthSec) },
        forwardOneHundredToOne = { player?.seekToOffset(10) },
        back1sec = { player?.seekToOffset((-1).sec) },
        backOneTenthSec = { player?.seekToOffset((-1).OneTenthSec) },
        backOneHundredToOne = { player?.seekToOffset(-10) },
        screenshots = {
            //
        },
    ) {
        VideoPlayer(videoUri) {
            player = it
        }
    }
}

@Preview(device = "spec:parent=pixel_5,orientation=landscape")
@Composable
fun DingzhenPage(
    forward10sec: () -> Unit = {},
    forward1sec: () -> Unit = {},
    forwardOneTenthSec: () -> Unit = {},
    forwardOneHundredToOne: () -> Unit = {},
    back1sec: () -> Unit = {},
    backOneTenthSec: () -> Unit = {},
    backOneHundredToOne: () -> Unit = {},
    screenshots: () -> Unit = {},
    player: @Composable () -> Unit = {},
) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black)
    ) {
        player()
        Column(modifier = Modifier.align(Alignment.TopStart)) {
            FloatingActionButton(
                onClick = back1sec,
                modifier = Modifier.padding(12.dp, 6.dp)
            ) {
                Text(text = "1秒")
            }
            FloatingActionButton(
                onClick = backOneTenthSec,
                modifier = Modifier.padding(12.dp, 6.dp)
            ) {
                Text(text = "1/10秒")
            }
            FloatingActionButton(
                onClick = backOneHundredToOne,
                modifier = Modifier.padding(12.dp, 6.dp)
            ) {
                Text(text = "1/100")
            }
            FloatingActionButton(
                onClick = screenshots,
                modifier = Modifier.padding(12.dp, 6.dp)
            ) {
                Text(text = "截屏")
            }
        }
        Column(modifier = Modifier.align(Alignment.TopEnd)) {
            FloatingActionButton(
                onClick = forward10sec,
                modifier = Modifier.padding(12.dp, 6.dp)
            ) {
                Text(text = "10秒")
            }
            FloatingActionButton(
                onClick = forward1sec,
                modifier = Modifier.padding(12.dp, 6.dp)
            ) {
                Text(text = "1秒")
            }
            FloatingActionButton(
                onClick = forwardOneTenthSec,
                modifier = Modifier.padding(12.dp, 6.dp)
            ) {
                Text(text = "1/10秒")
            }
            FloatingActionButton(
                onClick = forwardOneHundredToOne,
                modifier = Modifier.padding(12.dp, 6.dp)
            ) {
                Text(text = "1/100")
            }
        }
    }
}

fun ExoPlayer.seekToOffset(offsetMs: Long) {
    var positionMs: Long = currentPosition + offsetMs
    val durationMs: Long = duration
    if (durationMs != C.TIME_UNSET) {
        positionMs = positionMs.coerceAtMost(durationMs)
    }
    positionMs = positionMs.coerceAtLeast(0)
    pause()
    seekTo(positionMs)
}