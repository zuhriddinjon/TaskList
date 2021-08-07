package uz.instat.tasklist.framework.datasource

import android.content.Context
import kotlinx.coroutines.flow.Flow
import uz.instat.tasklist.busines.cache.data.TaskCache
import uz.instat.tasklist.busines.interactors.DataState

interface MainDataSource {

    fun saveTask(task: TaskCache): Flow<DataState<Long>>

    fun getAllTasks(): Flow<DataState<List<TaskCache>>>

    fun getInProgressTasks(): Flow<DataState<List<TaskCache>>>

    fun getPerformedTasks(): Flow<DataState<List<TaskCache>>>

    fun finishTask(id: Long): Flow<DataState<Unit>>

    fun inProgressTask(id: Long): Flow<DataState<Unit>>

    fun getTask(id: Long): Flow<DataState<TaskCache>>

    fun deleteTask(id: Long): Flow<DataState<Unit>>

}
