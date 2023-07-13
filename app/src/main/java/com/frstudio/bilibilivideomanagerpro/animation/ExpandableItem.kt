package com.frstudio.bilibilivideomanagerpro.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

/**
 * 可展开的列表
 *
 * @param title 列表标题
 * @param modifier Modifier
 * @param endText 列表标题的尾部文字，默认为空
 * @param subItemStartPadding 子项距离 start 的 padding 值
 * @param subItem 子项
 * */
@Composable
fun ExpandableItem(
    title: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    endText: String = "",
    subItemStartPadding: Int = 8,
    subItem: @Composable () -> Unit
) {
    var isShowSubItem: Boolean by remember { mutableStateOf(false) }

    val arrowRotateDegrees: Float by animateFloatAsState(if (isShowSubItem) 90f else 0f)

    Column(modifier = modifier) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    isShowSubItem = !isShowSubItem
                }
        ) {
            title()
            Row {
                if (endText.isNotBlank()) {
                    Text(text = endText,
                        modifier = modifier.padding(end = 4.dp).widthIn(0.dp, 100.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis)
                }
                Icon(
                    Icons.Outlined.KeyboardArrowRight,
                    contentDescription = null,
                    modifier = Modifier.rotate(arrowRotateDegrees)
                )
            }
        }

        AnimatedVisibility(visible = isShowSubItem) {
            Column(modifier = Modifier.padding(start = subItemStartPadding.dp)) {
                subItem()
            }
        }
    }
}