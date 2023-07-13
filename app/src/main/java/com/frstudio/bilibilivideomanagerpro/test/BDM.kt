package com.frstudio.bilibilivideomanagerpro.test

import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.documentfile.provider.DocumentFile
import com.frstudio.bilibilivideomanagerpro.app
import com.frstudio.bilibilivideomanagerpro.utils.getFitSize
import master.flame.danmaku.controller.DrawHandler
import master.flame.danmaku.danmaku.model.BaseDanmaku
import master.flame.danmaku.danmaku.model.DanmakuTimer
import master.flame.danmaku.danmaku.model.IDanmakus
import master.flame.danmaku.danmaku.model.android.DanmakuContext
import master.flame.danmaku.danmaku.model.android.Danmakus
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser
import master.flame.danmaku.danmaku.parser.android.AndroidFileSource
import master.flame.danmaku.danmaku.parser.android.BiliDanmukuParser
import master.flame.danmaku.ui.widget.DanmakuView


//@Composable
//fun BDM(resultDM: (DanmakuView, DanmakuContext, ) -> Unit,){
////    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
////    val height = getFitSize(screenWidth, 1920, 1080)
//    BDM(
//        modifier = Modifier
////            .size(screenWidth, height)
//            .fillMaxSize()
//            .clip(MaterialTheme.shapes.large),
//        resultDM
//    )
//}

@Composable
fun BDM(
    modifier: Modifier = Modifier,
    danmaku: DocumentFile?,
    onPrepared: () -> Unit,
    resultDM: (DanmakuView, DanmakuContext, ) -> Unit,
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val height = getFitSize(screenWidth, 1920, 1080)
    AndroidView(
        factory = { context ->
            DanmakuView(context).apply {
                //设置最大显示行数
                val maxLInesPair = HashMap<Int, Int>(16)
                maxLInesPair[BaseDanmaku.TYPE_SCROLL_RL] = 8
                //设置是否禁止重叠
                val overlappingEnablePair = HashMap<Int, Boolean>(16)
                overlappingEnablePair[BaseDanmaku.TYPE_SCROLL_RL] = true
                overlappingEnablePair[BaseDanmaku.TYPE_FIX_TOP] = true
                //创建弹幕上下文
                val dContext = DanmakuContext.create()
                //设置一些相关的配置
                dContext.setDuplicateMergingEnabled(false) //是否重复合并
//                    .setScrollSpeedFactor(1.2f) //设置文字的比例
//                    .setScaleTextSize(1.2f) //图文混排的时候使用！这里可以不用
//                    .setCacheStuffer(MyCacheStuffer(mActivity), mBackgroundCacheStuffer) //设置显示最大行数
                    .setMaximumLines(maxLInesPair) //设置防，null代表可以重叠
                    .preventOverlapping(overlappingEnablePair)
                //设置解析器
                val danmakuParser = if (danmaku != null) BiliDanmukuParser().load(AndroidFileSource(
                    app.contentResolver.openInputStream(danmaku.uri)))//???为什么用uri.path
                else getDefaultDanmakuParser()
                danmakuParser.setConfig(dContext)
                //相应的回掉
                setCallback(object : DrawHandler.Callback {
                    override fun updateTimer(timer: DanmakuTimer) {
                        //定时器更新的时候回掉
                    }

                    override fun drawingFinished() {
                        //弹幕绘制完成时回掉
                    }

                    fun danmakuShown(danmaku: BaseDanmaku?) {
                        //弹幕展示的时候回掉
                    }

                    override fun prepared() {
                        //弹幕准备好的时候回掉，这里启动弹幕
                        onPrepared()
                    }
                })
                prepare(danmakuParser, dContext)
                enableDanmakuDrawingCache(true)

                resultDM(this, dContext)
            }
        },
        modifier = modifier.size(screenWidth, height)
    )
}


fun getDefaultDanmakuParser(): BaseDanmakuParser {
    return object : BaseDanmakuParser() {
        override fun parse(): IDanmakus {
            return Danmakus()
        }
    }
}