package uz.instat.tasklist.busines.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TaskLocal(
    var id: Long = 0L,
    var title: String,
    var description: String,
    var time: Long,
    var alarmTime: Long = 30 * 60 * 1000,
    var status: Int = 0  // 0 -> created, 1 -> in progress, 2 -> finished
) : Parcelable