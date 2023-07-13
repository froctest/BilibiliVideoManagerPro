package com.frstudio.bilibilivideomanagerpro.utils

fun storage(bytes: Long): String {
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    var size = bytes.toDouble()
    var unitIndex = 0

    while (size >= 1024 && unitIndex < units.size - 1) {
        size /= 1024
        unitIndex++
    }

    return "%.2f %s".format(size, units[unitIndex])
}
fun formatMilliseconds(milliseconds: Long): String {
    val seconds = (milliseconds / 1000) % 60
    val minutes = ((milliseconds / (1000 * 60)) % 60)
    val hours = (milliseconds / (1000 * 60 * 60))

    return when {
        hours > 0 -> String.format("%d:%02d:%02d", hours, minutes, seconds)
        minutes > 0 -> String.format("%d:%02d", minutes, seconds)
        else -> String.format("%d", seconds)
    }
}