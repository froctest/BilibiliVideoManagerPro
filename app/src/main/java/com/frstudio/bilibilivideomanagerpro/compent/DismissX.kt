package com.frstudio.bilibilivideomanagerpro.compent

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.DismissDirection
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.SwipeToDismiss
import androidx.compose.material.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class, ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.DismissX(dismiss: () -> Unit, block: @Composable () -> Unit) {
    // 侧滑删除所需State
    val dismissState = rememberDismissState()
    // 按指定方向触发删除后的回调，在此处变更具体数据
    if(dismissState.isDismissed(DismissDirection.StartToEnd)){
        dismiss()
    }
    SwipeToDismiss(
        state = dismissState,
        // animateItemPlacement() 此修饰符便添加了动画
        modifier = Modifier.animateItemPlacement(),
        // 下面这个参数为触发滑动删除的移动阈值
        dismissThresholds = { direction ->
            FractionalThreshold(if (direction == DismissDirection.StartToEnd) 0.25f else 0.5f)
        },
        // 允许滑动删除的方向
        directions = setOf(DismissDirection.StartToEnd),
        // "背景 "，即原来显示的内容被划走一部分时显示什么
        background = {
        /*保证观看体验，省略此处内容*/
        }
    ) {
        block()
    }
}