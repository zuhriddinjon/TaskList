package uz.instat.tasklist.busines.cache.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import uz.instat.tasklist.busines.cache.data.TaskCache

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskCache): Long

    @Query("select * from task")
    suspend fun getAllTasks(): List<TaskCache>

    @Query("select * from task where status == 1")
    suspend fun getInProgressTasks(): List<TaskCache>

    @Query("select * from task where status == 2")
    suspend fun getFinishTasks(): List<TaskCache>

    @Query("update task set status = 2 where _id=:id")
    suspend fun finishTask(id: Long): Unit

    @Query("update task set status = 1 where _id=:id")
    suspend fun inProgressTask(id: Long): Unit

    @Query("select * from task where _id=:id")
    suspend fun getTask(id: Long): TaskCache

    @Query("delete from task  where _id=:id")
    suspend fun deleteTask(id: Long)

}