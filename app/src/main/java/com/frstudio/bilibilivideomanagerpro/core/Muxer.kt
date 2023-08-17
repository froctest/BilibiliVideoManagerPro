package com.frstudio.bilibilivideomanagerpro.core

import android.annotation.SuppressLint
import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaMuxer
import android.util.Log
import com.frstudio.bilibilivideomanagerpro.utils.fd
import java.io.FileDescriptor
import java.nio.ByteBuffer

/**
 * 自动判断是否已经存在合成完成的视频,若存在,立即返回,不存在,放置缓存供下一次直接返回
 */
fun muxVideoAudio(page: BiliVideoProjectPage): FileDescriptor {
    val dcDir = page.pageDir.findFile(".dc")?:page.pageDir.createDirectory(".dc")!!
    var mux = dcDir.findFile("mux-${page.title}.mp4")
    if (mux != null) return mux.fd
    dcDir.findFile("tmp_mux-${page.title}.mp4")?.delete()
    mux = dcDir.createFile("video/mp4", "tmp_mux-${page.title}.mp4")!!
    val videoFD = page.justVideo.fd
    val audioFD = page.justAudio.fd
    val outputFD = mux.fd
    muxVideoAudio(videoFD, audioFD, outputFD)
    mux.renameTo("mux-${page.title}.mp4")
    try {
        outputFD.sync()
    }catch (e: Exception) {
        e.printStackTrace()
    }
    return outputFD
}

@SuppressLint("WrongConstant")//////
fun muxVideoAudio(videoFD: FileDescriptor, audioFD: FileDescriptor, outputFD: FileDescriptor) {
    try {
        val videoExtractor = MediaExtractor()
        videoExtractor.setDataSource(videoFD)
        val audioExtractor = MediaExtractor()
        audioExtractor.setDataSource(audioFD)
        val muxer = MediaMuxer(outputFD, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)
        videoExtractor.selectTrack(0)
        val videoFormat = videoExtractor.getTrackFormat(0)
        val videoTrack = muxer.addTrack(videoFormat)
        audioExtractor.selectTrack(0)
        val audioFormat = audioExtractor.getTrackFormat(0)
        val audioTrack = muxer.addTrack(audioFormat)
//        LogUtil.d(TAG, "Video Format $videoFormat")
//        LogUtil.d(TAG, "Audio Format $audioFormat")
        var sawEOS = false
        var frameCount = 0
        val offset = 100
        val sampleSize = 256 * 1024
        val videoBuf = ByteBuffer.allocate(sampleSize)
        val audioBuf = ByteBuffer.allocate(sampleSize)
        val videoBufferInfo = MediaCodec.BufferInfo()
        val audioBufferInfo = MediaCodec.BufferInfo()
        videoExtractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC)
        audioExtractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC)
        muxer.start()
        while (!sawEOS) {
            videoBufferInfo.offset = offset
            videoBufferInfo.size = videoExtractor.readSampleData(videoBuf, offset)
            if (videoBufferInfo.size < 0 || audioBufferInfo.size < 0) {
                sawEOS = true
                videoBufferInfo.size = 0
            } else {
                videoBufferInfo.presentationTimeUs = videoExtractor.sampleTime
                videoBufferInfo.flags = videoExtractor.sampleFlags
                muxer.writeSampleData(videoTrack, videoBuf, videoBufferInfo)
                videoExtractor.advance()
                frameCount++
            }
        }
        var sawEOS2 = false
        var frameCount2 = 0
        while (!sawEOS2) {
            frameCount2++
            audioBufferInfo.offset = offset
            audioBufferInfo.size = audioExtractor.readSampleData(audioBuf, offset)
            if (videoBufferInfo.size < 0 || audioBufferInfo.size < 0) {
                sawEOS2 = true
                audioBufferInfo.size = 0
            } else {
                audioBufferInfo.presentationTimeUs = audioExtractor.sampleTime
                audioBufferInfo.flags = audioExtractor.sampleFlags
                muxer.writeSampleData(audioTrack, audioBuf, audioBufferInfo)
                audioExtractor.advance()
            }
        }
        Log.e("DV","1")
        muxer.stop()
        Log.e("DV","2")
        muxer.release()
        Log.e("DV","3")
    } catch (e: Exception) {
        Log.d("Muxer", "Mixer Error 2 " + e.message)
    }
}