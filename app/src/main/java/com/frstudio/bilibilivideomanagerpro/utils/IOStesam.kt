package com.frstudio.bilibilivideomanagerpro.utils

import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.nio.channels.Channels
import java.nio.channels.WritableByteChannel
import java.util.Objects


@Throws(IOException::class)
fun InputStream.transfer(out: OutputStream): Long {
    Objects.requireNonNull(out, "out")
    var transferred: Long = 0
    val buffer = ByteArray(8192)
    var read: Int
    while (this.read(buffer, 0, 8192).also { read = it } >= 0) {
        out.write(buffer, 0, read)
        transferred += read.toLong()
    }
    return transferred
}

fun OutputStream.convertToChannel(): WritableByteChannel {
    return Channels.newChannel(this)
}