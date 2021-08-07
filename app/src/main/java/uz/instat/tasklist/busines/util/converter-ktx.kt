package uz.instat.tasklist.busines.util

import android.content.res.Resources
import android.graphics.Color
import java.text.SimpleDateFormat
import java.util.*

fun dpToPx(dp: Float): Float {
    return (dp * Resources.getSystem().displayMetrics.density)
}

fun dpToPxInt(dp: Float): Int {
    return (dp * Resources.getSystem().displayMetrics.density).toInt()
}

fun pxToDp(px: Int): Int {
    return (px / Resources.getSystem().displayMetrics.density).toInt()
}

fun isColorDark(color: Int): Boolean {
    val darkness =
        1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255
    return darkness >= 0.5
}

val Calendar.date: String
    get() {
        val format = "EEE, d MMM yyyy"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        return sdf.format(time)
    }

val Calendar.hour: String
    get() {
        val format = "hh:mm a"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        return sdf.format(time)
    }

val Long.date: String
    get() {
        val format = "EEE, d MMM yyyy"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        val date = Date(this)
        return sdf.format(date)
    }

val Long.hour: String
    get() {
        val format = "hh:mm a"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        val date = Date(this)
        return sdf.format(date)
    }