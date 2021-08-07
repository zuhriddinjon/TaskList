package uz.instat.tasklist.busines.util

import android.os.Environment
import android.util.Log
import uz.instat.tasklist.presentation.TaskApp
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException


fun clog(message: String?) {
    message?.let {
//        FirebaseCrashlytics.getInstance().log(it)
    }
}

fun Any.logi(message: Any?) = message?.let {
    val messageText = "${this.javaClass.name}:$message"
    Log.i("AppDebug", messageText)
    appendLog(messageText, false)
    clog(messageText)
}

fun Any.loge(message: Any?) = message?.let {
    val messageText =
        "${this.javaClass.name}:${if (message is Throwable) message.message else message}"
    Log.e(
        "AppDebugError",
        messageText,
        if (message is Throwable) message else null
    )
    appendLog(messageText, true)
    clog(message.toString())
}


fun appendLog(text: String?, isError: Boolean) {
    val mediaDir = TaskApp.appContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
    val dir = if (mediaDir != null && mediaDir.exists())
        mediaDir else TaskApp.appContext.filesDir
    val logFile = File(dir, if (isError) "error.txt" else "logs.txt")
    if (!logFile.exists()) {
        try {
            logFile.createNewFile()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
    try {
        //BufferedWriter for performance, true to set append to file flag
        val buf = BufferedWriter(FileWriter(logFile, true))
        buf.append(text)
        buf.newLine()
        buf.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}