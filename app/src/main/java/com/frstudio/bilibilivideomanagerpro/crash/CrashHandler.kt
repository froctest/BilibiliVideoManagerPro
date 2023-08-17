package com.frstudio.bilibilivideomanagerpro.crash

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.os.Process
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import com.frstudio.bilibilivideomanagerpro.App
import com.frstudio.bilibilivideomanagerpro.app
import org.apache.log4j.Logger
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date


/**
 * Created by BAIPEI on 2017/12/5.
 */


object CrashHandler : Thread.UncaughtExceptionHandler {
    private const val TAG = "CrashHandler"
    private val logger = Logger.getLogger(TAG)
    private var mDefaultHandler // 系统默认的UncaughtException处理类
            : Thread.UncaughtExceptionHandler? = null
    private lateinit var mContext // 程序的Context对象
            : App
    private val info: MutableMap<String, String> = HashMap() // 用来存储设备信息和异常信息
    private val format = SimpleDateFormat(
        "yyyy-MM-dd-HH-mm-ss"
    ) // 用于格式化日期,作为日志文件名的一部分

    /**
     * 初始化
     *
     * @param context
     */
    fun init(context: App) {
        mContext = context
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler() // 获取系统默认的UncaughtException处理器
        Thread.setDefaultUncaughtExceptionHandler(this) // 设置该CrashHandler为程序的默认处理器
    }

    /**
     * 当UncaughtException发生时会转入该重写的方法来处理
     */
    override fun uncaughtException(thread: Thread, ex: Throwable) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果自定义的没有处理则让系统默认的异常处理器来处理
            mDefaultHandler!!.uncaughtException(thread, ex)
        } else {
            try {
                Thread.sleep(3000) // 如果处理了，让程序继续运行3秒再退出，保证文件保存并上传到服务器
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            // 退出程序
            Process.killProcess(Process.myPid())
            System.exit(1)
        }
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
     *
     * @param ex
     * 异常信息
     * @return true 如果处理了该异常信息;否则返回false.
     */
    private fun handleException(ex: Throwable?): Boolean {
        if (ex == null) return false
        object : Thread() {
            override fun run() {
                Looper.prepare()
                Toast.makeText(mContext, "很抱歉,程序出现异常,即将退出", Toast.LENGTH_SHORT).show()
                Looper.loop()
            }
        }.start()
        // 收集设备参数信息
        collectDeviceInfo(mContext)
        // 保存日志文件
        runCrash(ex)
        return true
    }

    /**
     * 收集设备参数信息
     *
     * @param context
     */
    private fun collectDeviceInfo(context: Context?) {
        try {
            val pm = context!!.packageManager // 获得包管理器
            val pi = pm.getPackageInfo(
                context.packageName,
                PackageManager.GET_ACTIVITIES
            ) // 得到该应用的信息，即主Activity
            if (pi != null) {
                val versionName = if (pi.versionName == null) "null" else pi.versionName
                val versionCode = pi.versionCode.toString() + ""
                info["versionName"] = versionName
                info["versionCode"] = versionCode
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        val fields = Build::class.java.declaredFields // 反射机制
        for (field in fields) {
            try {
                field.isAccessible = true
                info[field.name] = field[""]!!.toString()
//                logger.warn(field.name + ":" + field[""])
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            }
        }
    }

    private fun runCrash(throwable: Throwable) {
        val time = format.format(Date())
        val crashFile = File(
            app.getExternalFilesDir("Crash"),
            "crash_$time.log"
        )
        var versionName = "unknown"
        var versionCode: Long = 0
        try {
            val packageInfo: PackageInfo =
                app.packageManager.getPackageInfo(app.packageName, 0)
            versionName = packageInfo.versionName
            versionCode =
                packageInfo.longVersionCode
        } catch (ignored: PackageManager.NameNotFoundException) {
        }
        var fullStackTrace: String
        run {
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            throwable.printStackTrace(pw)
            fullStackTrace = sw.toString()
            pw.close()
        }
        val sb = StringBuilder()
        sb.append("软件崩溃,请把以下内容发送给软件作者,有助于更快地修Bug\n")
        sb.append("************* Crash Head ****************\n")
        sb.append("Time Of Crash      : ").append(time).append("\n")
        sb.append("Device Manufacturer: ").append(Build.MANUFACTURER).append("\n")
        sb.append("Device Model       : ").append(Build.MODEL).append("\n")
        sb.append("Android Version    : ").append(Build.VERSION.RELEASE).append("\n")
        sb.append("Android SDK        : ").append(Build.VERSION.SDK_INT).append("\n")
        sb.append("App VersionName    : ").append(versionName).append("\n")
        sb.append("App VersionCode    : ").append(versionCode).append("\n")
        sb.append("************* Crash Head ****************\n")
        sb.append("\n")
            .append(fullStackTrace)
        val errorLog = sb.toString()
        logger.error(errorLog)
        try {
            writeFile(crashFile, errorLog)
        } catch (ignored: IOException) {
        }

        val intent = Intent(app, CrashActivity::class.java)
        intent.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        intent.putExtra(CrashActivity.EXTRA_CRASH_INFO, errorLog)
        app.startActivity(intent)
        Process.killProcess(Process.myPid())
        System.exit(0)
    }

    @Throws(IOException::class)
    private fun writeFile(file: File, content: String) {
        logger.error("Writing crash file: $file")
        val parentFile = file.parentFile
        if (parentFile != null && !parentFile.exists()) {
            parentFile.mkdirs()
        }
        file.createNewFile()
        val fos = FileOutputStream(file)
        fos.write(content.toByteArray())
        fos.close()
        logger.error("Write finish of crash file: $file")
    }


//    private fun runCrash(ex: Throwable): String? {
//        val sb = StringBuffer()
//        for ((key, value) in info) {
//            sb.append("$key=$value\r\n")
//        }
//        val writer: Writer = StringWriter()
//        val pw = PrintWriter(writer)
//        ex.printStackTrace(pw)
//        var cause = ex.cause
//        // 循环着把所有的异常信息写入writer中
//        while (cause != null) {
//            cause.printStackTrace(pw)
//            cause = cause.cause
//        }
//        pw.close() // 记得关闭
//        val result = writer.toString()
//        sb.append(result)
//        // 保存文件
//        val timetamp = System.currentTimeMillis()
//        val time = format.format(Date())
//
//        logger
//
//
//        val fileName = "crash-$time-$timetamp.log"
//        if (Environment.getExternalStorageState() ==
//            Environment.MEDIA_MOUNTED
//        ) {
//            try {
//                val dir =
//                    File(Environment.getExternalStorageDirectory().absolutePath + File.separator + "crash")
//                logger.error("CrashHandlerFile: $dir")
//                if (!dir.exists()) dir.mkdir()
//                val fos = FileOutputStream(
//                    File(
//                        dir,
//                        fileName
//                    )
//                )
//                fos.write(sb.toString().toByteArray())
//                fos.close()
//                return fileName
//            } catch (e: FileNotFoundException) {
//                e.printStackTrace()
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
//        }
//        return null
//    }


    class CrashActivity() : Activity(), MenuItem.OnMenuItemClickListener {
        private var mLog: String? = null
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setTheme(android.R.style.Theme_DeviceDefault)
            mLog = intent.getStringExtra(EXTRA_CRASH_INFO)
            run{
                val contentView = ScrollView(this)
                contentView.isFillViewport = true
                val hw = HorizontalScrollView(this)
                val message = TextView(this)
                run {
                    val padding: Int = dp2px(16f)
                    message.setPadding(padding, padding, padding, padding)
                    message.text = mLog
                    message.setTextIsSelectable(true)
                }
                hw.addView(message)
                contentView.addView(
                    hw,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setContentView(contentView)
            }
        }

        override fun onBackPressed() {
            restart()
        }

        private fun restart() {
            val pm = packageManager
            val intent = pm.getLaunchIntentForPackage(packageName)
            if (intent != null) {
                intent.addFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK
                            or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
                )
                startActivity(intent)
            }
            finish()
            Process.killProcess(Process.myPid())
            System.exit(0)
        }

        private fun dp2px(dpValue: Float): Int {
            val scale = Resources.getSystem().displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }

        override fun onMenuItemClick(item: MenuItem): Boolean {
            when (item.itemId) {
                android.R.id.copy -> {
                    val cm: ClipboardManager =
                        getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
                    cm.setPrimaryClip(ClipData.newPlainText(packageName, mLog))
                }
            }
            return false
        }

        override fun onCreateOptionsMenu(menu: Menu): Boolean {
            menu.add(0, android.R.id.copy, 0, android.R.string.copy).setOnMenuItemClickListener(this)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
            return true
        }

        companion object {
            val EXTRA_CRASH_INFO = "crashInfo"
        }
    }
}