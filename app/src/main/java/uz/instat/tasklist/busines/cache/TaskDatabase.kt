package uz.instat.tasklist.busines.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.instat.tasklist.busines.cache.dao.TaskDao
import uz.instat.tasklist.busines.cache.data.TaskCache

@Database(
    entities = [TaskCache::class],
    version = 1,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun todoDao(): TaskDao

    companion object {
        const val DB_NAME = "todo_db"
    }
}