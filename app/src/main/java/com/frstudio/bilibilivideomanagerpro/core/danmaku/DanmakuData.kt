package com.frstudio.bilibilivideomanagerpro.core.danmaku

import androidx.compose.runtime.mutableStateListOf
import androidx.documentfile.provider.DocumentFile
import com.frstudio.bilibilivideomanagerpro.utils.first
import com.frstudio.bilibilivideomanagerpro.utils.forEach
import com.frstudio.bilibilivideomanagerpro.utils.input
import java.io.File
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory

data class DanmakuData(val maxLimit: Int, val danmakus: List<BiliDanmaku>) {
    val size = danmakus.size

    companion object {
        val DEFAULT = DanmakuData(114514, listOf(
            BiliDanmaku("4783783427878234", "我是弹幕")
        ))
    }
}

fun DanmakuData(danmaku: File) = DanmakuData(danmaku.inputStream())
fun DanmakuData(danmaku: DocumentFile) = DanmakuData(danmaku.input!!)
fun DanmakuData(danmaku: InputStream): DanmakuData{
    val docFac = DocumentBuilderFactory.newInstance()
    val doc = docFac.newDocumentBuilder().parse(danmaku)
    doc.documentElement.normalize()
    val doc_i = doc.documentElement
    val maxLimit = doc_i.getElementsByTagName("maxlimit").first.textContent.toInt()
    val danmakus: MutableList<BiliDanmaku> = mutableStateListOf()
    doc_i.getElementsByTagName("d").forEach {
        danmakus.add(BiliDanmaku(it))
    }
    return DanmakuData(maxLimit, danmakus)
}