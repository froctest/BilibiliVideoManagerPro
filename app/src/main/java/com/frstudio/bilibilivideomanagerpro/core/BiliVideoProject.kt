package com.frstudio.bilibilivideomanagerpro.core

import androidx.compose.runtime.toMutableStateList
import androidx.documentfile.provider.DocumentFile
import com.frstudio.bilibilivideomanagerpro.app
import com.google.gson.Gson

fun getBiliVideoProject(dir: DocumentFile) = BiliVideoProject(dir)

data class BiliVideoProject(val root: DocumentFile) {
    val exists get() = root.exists()
    fun delete() {
        root.delete()
    }
    val pages = root.listFiles().mapNotNull {
        try {
            BiliVideoProjectPage(it)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }.toMutableStateList()
    val pageCount = root.listFiles().filter { it.isDirectory }.size
    val bvid by lazy {
        firstPageEntry.bvid
    }
    val firstPage = root.listFiles().firstNotNullOf {
        try {
            BiliVideoProjectPage(it)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    val firstPageEntry = firstPage.entry
    val entry: Entry by lazy {
        val fEntry = firstPage.entry
        Entry(
            fEntry.title,
            fEntry.cover,
            fEntry.total_time_milli,
            fEntry.total_bytes,
            fEntry.downloaded_bytes,
            fEntry.owner_name,
            fEntry.owner_avatar?:"sdafasfdfdas"
        )
    }
    data class Entry(
        val title: String,
        val cover: String,
        val total_time_milli: Long,
        val total_bytes: Long,
        val downloaded_bytes: Long,
        val owner_name: String,
        val owner_avatar: String,
    )

    override fun equals(other: Any?): Boolean {
        if (other !is BiliVideoProject) return false
        return bvid == other.bvid
    }

    override fun hashCode(): Int {
        return firstPageEntry.bvid.hashCode()
    }
}

data class BiliVideoProjectPage(val pageDir: DocumentFile) {
    val entryJsonFile = pageDir.findFile("entry.json")!!
    val danmakuFile = pageDir.findFile("danmaku.xml")
    val videoDir = pageDir.listFiles().filter { it.isDirectory }[0]
    val justVideo = videoDir.findFile("video.m4s")!!
    val justAudio = videoDir.findFile("audio.m4s")!!

    val entry: Entry by lazy {
        val input = app.contentResolver.openInputStream(entryJsonFile.uri)!!
        Gson().fromJson(input.reader(), Entry::class.java)
    }
    val pageData: PageData by lazy {
        entry.page_data
    }
    val dc: DocumentFile? get() = pageDir.findFile(".dc")?.let { if (it.isDirectory) it else null }
    val mux: DocumentFile? get() = dc?.findFile("mux-$title.mp4")


    val title: String = entry.title
    val partTitle: String = entry.page_data.part?:title

    override fun equals(other: Any?): Boolean {
        if (other !is BiliVideoProjectPage) return false
        return entry.bvid == other.entry.bvid && entry.page_data.page == other.entry.page_data.page
    }

    override fun hashCode(): Int {
        return entry.bvid.hashCode()
    }
}
data class Entry(
    val media_type: Int,
    val has_dash_audio: Boolean,
    val is_completed: Boolean,//是否下载完成
    val total_bytes: Long,//总字节
    val downloaded_bytes: Long,//已下载字节
    val title: String,//标题(主标题)
    val type_tag: String,
    val cover: String,//封面Url
    val video_quality: Int,
    val prefered_video_quality: Int,
    val guessed_total_bytes: Long,
    val total_time_milli: Long,//视频时长
    val danmaku_count: Long,//弹幕数量
    val time_update_stamp: Long,//更新日期
    val time_create_stamp: Long,//创建日期
    val can_play_in_advance: Boolean,//可以提前播放
    val interrupt_transform_temp_file: Boolean,
    val quality_pithy_description: String,//视频清晰度
    val quality_superscript: String,
    val cache_version_code: Long,
    val preferred_audio_quality: Int,
    val audio_quality: Int,
    val avid: Long,//视频AV号
    val spid: Long,
    val seasion_id: Long,
    val bvid: String,//视频BV号
    val owner_id: Long,//作者Id
    val owner_name: String,//作者名称
    val owner_avatar: String?,//作者头像Url
    //TODO 分集数据 page_data
    val page_data: PageData
) {
}
data class PageData(
    val cid: Long,
    val page: Int,//序号,从1开始
    val from: String,
    val part: String?,//标题
    val width: Long,
    val height: Long,
    val rotate: Float//不清楚类型,应该是Float
)