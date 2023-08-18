import android.net.Uri
import android.util.Log
import com.frstudio.bilibilivideomanagerpro.utils.convertToChannel
import com.frstudio.bilibilivideomanagerpro.utils.documentFile
import com.frstudio.bilibilivideomanagerpro.utils.output
import com.googlecode.mp4parser.authoring.Track
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack
import java.io.File
import java.io.IOException
import java.util.Arrays
import java.util.LinkedList


/**
 * Author: AlanWang4523.
 * Date: 17/3/21 11:40.
 * Mail: alanwang6584@gmail.com
 */
object VideoClipUtils {
    private val TAG = VideoClipUtils::class.java.simpleName

    /**
     * 裁剪视频
     * @param srcPath 需要裁剪的原视频路径
     * @param outPath 裁剪后的视频输出路径
     * @param startTimeMs 裁剪的起始时间
     * @param endTimeMs 裁剪的结束时间
     */
    @Throws(IOException::class, IllegalArgumentException::class)
    fun clip(srcPath: File, outPath: Uri, startPoint: Long, endPoint: Long) {
        require(srcPath.exists()) { "the source file is not exist!!!!" }
        require(startPoint < endPoint) { "the startTimeMs is larger than endTimeMs!!!!" }
        val movie = MovieCreator.build(srcPath.toString())
        val tracks = movie.tracks
        //移除旧的track
        movie.tracks = LinkedList()
        //处理的时间以秒为单位
        var startTime = startPoint.toDouble() / 1000
        var endTime = endPoint.toDouble() / 1000
        Log.d(
            TAG, """--->>>>startTimeMs = $startPoint
 endTimeMs = $endPoint
 tracks.size = ${tracks.size}"""
        )
        //计算剪切时间，视频的采样间隔大，以视频为准
        for (track in tracks) {
            if (track.syncSamples != null && track.syncSamples.isNotEmpty()) {
                startTime = correctTimeToSyncSample(track, startTime, false)
                endTime = correctTimeToSyncSample(track, endTime, true)
                if (track.handler == "vide") {
                    break
                }
            }
        }
        Log.d(TAG, "--->>>>startTime = $startTime\n endTime = $endTime")
        var currentSample: Long
        var currentTime: Double
        var lastTime: Double
        var startSample1: Long
        var endSample1: Long
        var delta: Long
        for (track in tracks) {
            currentSample = 0
            currentTime = 0.0
            lastTime = -1.0
            startSample1 = -1
            endSample1 = -1

            //根据起始时间和截止时间获取起始sample和截止sample的位置
            for (i in track.sampleDurations.indices) {
                delta = track.sampleDurations[i]
                if (currentTime > lastTime && currentTime <= startTime) {
                    startSample1 = currentSample
                }
                if (currentTime > lastTime && currentTime <= endTime) {
                    endSample1 = currentSample
                }
                lastTime = currentTime
                currentTime += delta.toDouble() / track.trackMetaData.timescale.toDouble()
                currentSample++
            }
            Log.d(
                TAG, """track.getHandler() = ${track.handler}
 startSample1 = $startSample1
 endSample1 = $endSample1"""
            )
            if (startSample1 <= 0 && endSample1 <= 0) {
                throw RuntimeException("clip failed !!")
            }
            movie.addTrack(CroppedTrack(track, startSample1, endSample1)) // 添加截取的track
        }

        //合成视频mp4
        val out = DefaultMp4Builder().build(movie)
        val fos = outPath.documentFile!!.output
        val fco = fos.convertToChannel()
        out.writeContainer(fco)
        fco.close()
        fos.close()
    }

    /**
     * 换算剪切时间
     * @param track
     * @param cutHere
     * @param next
     * @return
     */
    private fun correctTimeToSyncSample(track: Track, cutHere: Double, next: Boolean): Double {
        val timeOfSyncSamples = DoubleArray(track.syncSamples.size)
        var currentSample: Long = 0
        var currentTime = 0.0
        for (i in track.sampleDurations.indices) {
            val delta = track.sampleDurations[i]
            val index = Arrays.binarySearch(track.syncSamples, currentSample + 1)
            if (index >= 0) {
                timeOfSyncSamples[index] = currentTime
            }
            currentTime += (delta.toDouble() / track.trackMetaData.timescale.toDouble())
            currentSample++
        }
        var previous = 0.0
        for (timeOfSyncSample in timeOfSyncSamples) {
            if (timeOfSyncSample > cutHere) {
                return if (next) {
                    timeOfSyncSample
                } else {
                    previous
                }
            }
            previous = timeOfSyncSample
        }
        return timeOfSyncSamples[timeOfSyncSamples.size - 1]
    }
}