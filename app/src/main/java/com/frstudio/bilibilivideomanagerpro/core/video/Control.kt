package com.frstudio.bilibilivideomanagerpro.core.video

import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import com.frstudio.bilibilivideomanagerpro.core.video.volume.VolumeUtil

@Composable
fun Control(player: @Composable ((ExoPlayer) -> Unit) -> Unit) {
    var exoPlayer: ExoPlayer? = null
    val configuration = LocalConfiguration.current
    val width = configuration.screenWidthDp.dp
    val height = configuration.screenHeightDp.dp
    val volumeControlEnabled = remember { mutableStateOf(false) }
    var offset by remember { mutableStateOf(Offset(0f,0f)) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragStart = { start ->
                        offset = start
                        volumeControlEnabled.value = true
                    },
                    onVerticalDrag = { change, _ ->
                        if (offset.x.dp > width) {
                            VolumeUtil.setMediaVolume(VolumeUtil.getMediaVolume())
                        }
                    },
                    onDragEnd = {
                        volumeControlEnabled.value = false
                    }
                )
            }
    ) {
        player {
            exoPlayer = it
        }
    }
}