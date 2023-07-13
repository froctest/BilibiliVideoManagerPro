package com.frstudio.bilibilivideomanagerpro

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.documentfile.provider.DocumentFile
import com.frstudio.bilibilivideomanagerpro.compent.OnBackPress
import com.frstudio.bilibilivideomanagerpro.core.BiliVideoProject
import com.frstudio.bilibilivideomanagerpro.core.BiliVideoProjectPage
import com.frstudio.bilibilivideomanagerpro.core.requireStoragePermission
import com.frstudio.bilibilivideomanagerpro.core.getBiliVideoProject
import com.frstudio.bilibilivideomanagerpro.core.muxVideoAudio
import com.frstudio.bilibilivideomanagerpro.ui.BiliVideoInfoPage
import com.frstudio.bilibilivideomanagerpro.ui.SizeAnimatedContent
import com.frstudio.bilibilivideomanagerpro.ui.WorkDialog
import com.frstudio.bilibilivideomanagerpro.ui.list.BiliVideoListItem
import com.frstudio.bilibilivideomanagerpro.ui.theme.BilibiliVideoManagerProTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            BilibiliVideoManagerProTheme {
                val projects = remember {
                    mutableStateListOf<BiliVideoProject>()
                }
                var biliDir: DocumentFile? by remember {
                    mutableStateOf(null)
                }
                val invoker = requireStoragePermission { dir: DocumentFile? ->
                    if (dir == null) {
                        Toast.makeText(app, "请选择正确位置", Toast.LENGTH_LONG).show()
                        return@requireStoragePermission
                    }
                    biliDir = dir
                }
                LaunchedEffect(key1 = biliDir) {
                    withContext(Dispatchers.IO) {
                        biliDir?.let {
                            it.listFiles().filter { it.isDirectory }.forEach { projectFile ->
                                try {
                                    val project = getBiliVideoProject(projectFile)
                                    if (projects.contains(project)) {
                                        projects.remove(project)
                                    }
                                    projects.add(project)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                }
                Column() {
                    if (biliDir == null) {
                        Button(onClick = { invoker() }, modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp)
                            .align(Alignment.CenterHorizontally)) {
                            Text(text = "选择")
                        }
                    }
                    var showProject: BiliVideoProject? by remember {
                        mutableStateOf(null)
                    }
                    OnBackPress {
                        if (showProject != null) showProject = null else {
                            context.startActivity(Intent(Intent.ACTION_MAIN).apply {
                                addCategory(Intent.CATEGORY_HOME)
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            })
                        }
                    }
                    if (biliDir != null) {
                        var refreshing by remember {
                            mutableStateOf(false)
                        }
                        val scope = rememberCoroutineScope()
                        val state = rememberPullRefreshState(refreshing = refreshing, onRefresh = {
                            scope.launch {
                                withContext(Dispatchers.IO) {
                                    refreshing = true
                                    biliDir?.let {
                                        it.listFiles().filter { it.isDirectory }.forEach { projectFile ->
                                            try {
                                                val project = getBiliVideoProject(projectFile)
                                                if (projects.contains(project)) {
                                                    projects.remove(project)
                                                }
                                                projects.add(project)
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                            }
                                        }
                                    }
                                    refreshing = false
                                }
                            }
                        })
                        Box(
                            Modifier
                                .fillMaxSize()
                                .pullRefresh(state)) {
                            LazyColumn() {
                                items(projects, key = {
                                    it.bvid
                                }) {project ->
                                    Card(modifier = Modifier.padding(4.dp)) {
                                        var progress by remember {
                                            mutableStateOf(0f)
                                        }
                                        val total by remember {
                                            mutableStateOf(project.pages.size)
                                        }
                                        LaunchedEffect(key1 = Unit) {
                                            withContext(Dispatchers.IO) {
                                                for (i in 0 until total) {
                                                    muxVideoAudio(project.pages[i])
                                                    progress = total / (i+1).toFloat()
                                                }
                                                progress = 1f
                                            }
                                        }
                                        SizeAnimatedContent(targetValue = (showProject == project), trueContent = {
                                            SizeAnimatedContent(
                                                targetValue = progress != 1f,
                                                trueContent = {
                                                    if (total == 1) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                                                    else LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), progress = progress)
                                                    Text(text = project.entry.title)
                                                }
                                            ) {
                                                var willFixPage: BiliVideoProjectPage? by remember {
                                                    mutableStateOf(null)
                                                }
                                                BiliVideoInfoPage(project = project, fix = {
                                                    willFixPage = project.pages[it]
                                                }) {
                                                    showProject = if (showProject == project) null else project
                                                }
                                                willFixPage?.run{
                                                    Fix(page = this) {
                                                        willFixPage = null
                                                    }
                                                }
                                            }
                                        }) {
                                            if (total == 1) LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                                            else LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), progress = progress)
                                            BiliVideoListItem(project = project) {
                                                showProject = if (showProject == project) null else project
                                            }
                                        }
                                    }
                                }
                            }
                            PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Fix(page: BiliVideoProjectPage, close: () -> Unit) {
    var step by remember {
        mutableStateOf(1)
    }
    var finished by remember {
        mutableStateOf(false)
    }
    WorkDialog(
        work = {
            withContext(Dispatchers.IO) {
                page.dc?.delete()
                step = 2
                try {
                    muxVideoAudio(page)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                finished = true
            }
        },
        title = {
            if (finished) Text(text = "修复完成", style = MaterialTheme.typography.bodyLarge)
            else {
                LinearProgressIndicator(progress = 3F/step, Modifier.padding(5.dp))
                Text(text = "修复中$step/2", style = MaterialTheme.typography.bodyLarge)
            }
        },
        text = {
            if (finished) Text(text = "修复完成")
            else {
                LinearProgressIndicator()
                Text(text = "正在修复中...")
            }
        },
        confirmButton = {
            if (finished) {
                Button(onClick = close) {
                    Text(text = "关闭")
                }
            }
        }
    , close)
}