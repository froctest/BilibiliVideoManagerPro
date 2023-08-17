package com.frstudio.bilibilivideomanagerpro.core.video.volume

import android.app.Service
import android.media.AudioManager
import com.frstudio.bilibilivideomanagerpro.app


object VolumeUtil {
    private val mAudioManager: AudioManager by lazy {
        app.getSystemService(Service.AUDIO_SERVICE) as AudioManager
    }

    //获取最大多媒体音量
    fun getMediaMaxVolume(): Int {
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
    }

    //获取当前多媒体音量
    fun getMediaVolume(): Int {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    }

    //获取最大通话音量
    fun getCallMaxVolume(): Int {
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)
    }

    //获取当前通话音量
    fun getCallVolume(): Int {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL)
    }

    //获取最大系统音量
    fun getSystemMaxVolume(): Int {
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM)
    }

    //获取当前系统音量
    fun getSystemVolume(): Int {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM)
    }

    //获取最大提示音量
    fun getAlermMaxVolume(): Int {
        return mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM)
    }

    //获取当前提示音量
    fun getAlermVolume(): Int {
        return mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM)
    }

    // 设置多媒体音量
    fun setMediaVolume(volume: Int) {
        mAudioManager.setStreamVolume(
            AudioManager.STREAM_MUSIC,  //音量类型
            volume, AudioManager.FLAG_PLAY_SOUND
                    or AudioManager.FLAG_SHOW_UI
        )
    }

    //设置通话音量
    fun setCallVolume(volume: Int) {
        mAudioManager.setStreamVolume(
            AudioManager.STREAM_VOICE_CALL,
            volume, AudioManager.FLAG_PLAY_SOUND
                    or AudioManager.FLAG_SHOW_UI
        )
    }

    //设置提示音量
    fun setAlermVolume(volume: Int) {
        mAudioManager.setStreamVolume(
            AudioManager.STREAM_ALARM,
            volume, AudioManager.FLAG_PLAY_SOUND
                    or AudioManager.FLAG_SHOW_UI
        )
    }

    // 关闭/打开扬声器播放
    fun setSpeakerStatus(on: Boolean) {
        if (on) { //扬声器
            mAudioManager.isSpeakerphoneOn = true
            mAudioManager.mode = AudioManager.MODE_NORMAL
        } else {
            // 设置最大音量
            val max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)
            mAudioManager.setStreamVolume(
                AudioManager.STREAM_VOICE_CALL,
                max,
                AudioManager.STREAM_VOICE_CALL
            )
            // 设置成听筒模式
            mAudioManager.mode = AudioManager.MODE_IN_COMMUNICATION
            mAudioManager.isSpeakerphoneOn = false // 关闭扬声器
            mAudioManager.setRouting(
                AudioManager.MODE_NORMAL,
                AudioManager.ROUTE_EARPIECE,
                AudioManager.ROUTE_ALL
            )
        }
    }
}
