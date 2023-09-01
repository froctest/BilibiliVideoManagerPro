package com.frstudio.bilibilivideomanagerpro.ui.list

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.frstudio.bilibilivideomanagerpro.compent.DismissX
import com.frstudio.bilibilivideomanagerpro.core.BiliVideoProject
import com.frstudio.bilibilivideomanagerpro.core.NetworkImage
import com.frstudio.bilibilivideomanagerpro.utils.formatMilliseconds
import com.frstudio.bilibilivideomanagerpro.utils.storage

@Composable
fun LazyItemScope.BiliVideoListItem(modifier: Modifier = Modifier, project: BiliVideoProject, dismiss: (BiliVideoProject) -> Unit = {}, clicked: () -> Unit) {
    val entry = project.entry
    BiliVideoListItem(
        modifier = modifier,
        entry.title,
        entry.cover,
        entry.total_time_milli,
        entry.downloaded_bytes,
        entry.total_bytes,
        entry.owner_name,
        entry.owner_avatar,
        pageCount = project.pageCount,
        dismiss = {
            dismiss(project)
        },
        clicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun LazyItemScope.BiliVideoListItem(
    modifier: Modifier = Modifier,
    title: String = "视频标题",
    coverUrl: String = "http://i2.hdslb.com/bfs/archive/a7feade2d3464bce826f2e48e7699fe0bab2a411.jpg",
    duration: Long = 78433844,
    downloadBytes: Long = 114514,
    totalBytes: Long = 114514,
    upName: String = "UP主",
    owner_avatarUrl: String = "https://i0.hdslb.com/bfs/face/e24e4ac984f7d29f88ff279e14fd4bb2a609d06d.jpg",
    pageCount: Int = 3,
    dismiss: () -> Unit = {},
    clicked: () -> Unit = {}
) {
    Card(modifier = modifier
        .fillMaxWidth()
        .animateContentSize(), onClick = clicked) {
        VideoInfo(title, coverUrl, duration, downloadBytes, totalBytes, pageCount, dismiss)
        UpInfo(Modifier.padding(6.dp), upName, owner_avatarUrl)
    }
}

@Preview
@Composable
fun LazyItemScope.VideoInfo(
    title: String = "视频标题",
    coverUrl: String = "http://i2.hdslb.com/bfs/archive/a7feade2d3464bce826f2e48e7699fe0bab2a411.jpg",
    duration: Long = 67437834,
    downloadBytes: Long = 114514,
    totalBytes: Long = 114514,
    pageCount: Int = 3,
    dismiss: () -> Unit = {}
) {
    Column() {
        Row() {
            DismissX(dismiss) {
                NetworkImage(url = coverUrl, modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape))
            }
            Column() {
                Text(text = title)
                Row() {
                    Text(text = "时长: ${formatMilliseconds(duration)}")
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = if (downloadBytes == totalBytes) storage(totalBytes) else "${storage(downloadBytes)} / ${storage(totalBytes)}")
                    if (pageCount != 1) Text(text = "共${pageCount}集")
                }
            }
        }
    }
}


@Preview
@Composable
fun UpInfo(modifier: Modifier = Modifier, name: String = "UP主", avatarUrl: String = "https://i0.hdslb.com/bfs/face/e24e4ac984f7d29f88ff279e14fd4bb2a609d06d.jpg") {
    Row(modifier) {
        NetworkImage(url = avatarUrl, modifier = Modifier
            .size(40.dp)
            .padding(5.dp)
            .clip(CircleShape))
        Column {
            Text(text = name)
        }
    }
}