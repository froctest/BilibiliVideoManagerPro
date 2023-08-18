package com.frstudio.bilibilivideomanagerpro.utils

import androidx.compose.ui.unit.Dp

fun getFitSize(length: Dp, percentLength: Int, percentOut: Int):Dp {
    val percent = length / percentLength
    return percent * percentOut
}