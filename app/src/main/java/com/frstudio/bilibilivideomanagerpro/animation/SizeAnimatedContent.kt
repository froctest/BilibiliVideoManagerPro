package com.frstudio.bilibilivideomanagerpro.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SizeAnimatedContent(targetValue: Boolean, trueContent: @Composable () ->Unit, falseContent: @Composable () ->Unit) {
    AnimatedContent(targetState = targetValue,
        transitionSpec = {
            fadeIn(animationSpec = tween(150, 150)) with
                    fadeOut(animationSpec = tween(150)) using
                    SizeTransform{ initialSize: IntSize, targetSize: IntSize ->
                        if (targetState)
                            keyframes {
                                IntSize(targetSize.width, initialSize.height)
                            }
                        else
                            keyframes {
                                IntSize(initialSize.width, targetSize.height)
                            }
                    }
        }) { value ->
        if (value) trueContent()
        else falseContent()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SizeAnimatedContent() {
    var show: Boolean by remember {
        mutableStateOf(false)
    }
    Surface(onClick = { show = !show }) {
        SizeAnimatedContent(targetValue = show, trueContent = {
            Text(text = "fgghfgh")
        }) {
            Text(text = "fyhygkjykjyggggyfyfyfffyfyfygfyfyfyjfyjfykygjyugfygfyffthgfyfffgfdgdfhgdfhgfghfgjhygjgyfhgfghfygytjfhdgfxcfghftghffyfyhygkjykjyggggyfyfyfffyfyfygfyfyfyjfyjfykygjyugfygfyffthgfyfffgfdgdfhgdfhgfghfgjhygjgyfhgfghfygytjfhdgfxcfghftghffyhfyhygkjykjyggggyfyfyfffyfyfygfyfyfyjfyjfykygjyugfygfyffthgfyfffgfdgdfhgdfhgfghfgjhygjgyfhgfghfygytjfhdgfxcfghftghffyhfyhygkjykjyggggyfyfyfffyfyfygfyfyfyjfyjfykygjyugfygfyffthgfyfffgfdgdfhgdfhgfghfgjhygjgyfhgfghfygytjfhdgfxcfghftghffyhfyhygkjykjyggggyfyfyfffyfyfygfyfyfyjfyjfykygjyugfygfyffthgfyfffgfdgdfhgdfhgfghfgjhygjgyfhgfghfygytjfhdgfxcfghftghffyhfyhygkjykjyggggyfyfyfffyfyfygfyfyfyjfyjfykygjyugfygfyffthgfyfffgfdgdfhgdfhgfghfgjhygjgyfhgfghfygytjfhdgfxcfghftghffyhfyhygkjykjyggggyfyfyfffyfyfygfyfyfyjfyjfykygjyugfygfyffthgfyfffgfdgdfhgdfhgfghfgjhygjgyfhgfghfygytjfhdgfxcfghftghffyhfyhygkjykjyggggyfyfyfffyfyfygfyfyfyjfyjfykygjyugfygfyffthgfyfffgfdgdfhgdfhgfghfgjhygjgyfhgfghfygytjfhdgfxcfghftghffyhfyhygkjykjyggggyfyfyfffyfyfygfyfyfyjfyjfykygjyugfygfyffthgfyfffgfdgdfhgdfhgfghfgjhygjgyfhgfghfygytjfhdgfxcfghftghffyhfyhygkjykjyggggyfyfyfffyfyfygfyfyfyjfyjfykygjyugfygfyffthgfyfffgfdgdfhgdfhgfghfgjhygjgyfhgfghfygytjfhdgfxcfghftghffyhfyhygkjykjyggggyfyfyfffyfyfygfyfyfyjfyjfykygjyugfygfyffthgfyfffgfdgdfhgdfhgfghfgjhygjgyfhgfghfygytjfhdgfxcfghftghffyhfyhygkjykjyggggyfyfyfffyfyfygfyfyfyjfyjfykygjyugfygfyffthgfyfffgfdgdfhgdfhgfghfgjhygjgyfhgfghfygytjfhdgfxcfghftghffyhh")
        }
    }
}