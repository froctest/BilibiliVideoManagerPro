package com.frstudio.bilibilivideomanagerpro.ui

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.frstudio.bilibilivideomanagerpro.R

@Composable
fun GreenPoint(modifier: Modifier = Modifier) {
    Icon(painter = painterResource(id = R.drawable.greenpoint), contentDescription = "", modifier, tint = Color.Green)
//    Canvas(modifier = modifier) {
//        drawCircle(
//            color = Color.Green,
//            radius = 10.dp.toPx(),
//            center = Offset(size.width / 2, size.height / 2),
//            style = Stroke(width = 0.dp.toPx())
//        )
//    }
}