package uz.instat.tasklist.busines.cache.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task")
data class TaskCache(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "_id")
    var id: Long = 0L,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "description")
    var description: String,
    @ColumnInfo(name = "time")
    var time: Long,
    @ColumnInfo(name = "alarm_time")
    var alarmTime: Long = 30 * 60 * 1000,
    @ColumnInfo(name = "status")
    var status: Int = 0  // 0 -> created, 1 -> in progress, 2 -> finished
)