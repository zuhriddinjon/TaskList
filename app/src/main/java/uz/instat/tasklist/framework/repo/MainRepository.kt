package uz.instat.tasklist.framework.repo

import android.content.Context
import kotlinx.coroutines.flow.Flow
import uz.instat.tasklist.busines.interactors.UiState
import uz.instat.tasklist.busines.local.TaskLocal


interface MainRepository {

    fun saveTask(task: TaskLocal): Flow<UiState<Long>>

    fun getAllTasks(): Flow<UiState<List<TaskLocal>>>

    fun getInProgressTasks(): Flow<UiState<List<TaskLocal>>>

    fun getPerformedTasks(): Flow<UiState<List<TaskLocal>>>

    fun finishTask(id: Long): Flow<UiState<Unit>>

    fun inProgressTask(id: Long): Flow<UiState<Unit>>

    fun getTask(id: Long): Flow<UiState<TaskLocal>>

    fun deleteTask(id: Long): Flow<UiState<Unit>>

}