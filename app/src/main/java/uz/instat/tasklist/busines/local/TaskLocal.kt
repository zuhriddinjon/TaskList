package uz.instat.tasklist.busines.local

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import uz.instat.tasklist.busines.enums.TaskStatus

@Parcelize
data class TaskLocal(
    var id: Long = 0L,
    var title: String,
    var description: String,
    var time: Long,
    var alarmTime: Long = 30 * 60 * 1000,
    var status: TaskStatus = TaskStatus.CREATE  // 0 -> created, 1 -> in progress, 2 -> finished
) : Parcelable