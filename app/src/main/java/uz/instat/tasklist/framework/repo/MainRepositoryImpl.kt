package uz.instat.tasklist.framework.repo

import android.content.Context
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uz.instat.tasklist.busines.cache.data.TaskCache
import uz.instat.tasklist.busines.interactors.DataStateHandler
import uz.instat.tasklist.busines.interactors.UiState
import uz.instat.tasklist.busines.local.TaskCacheMapper
import uz.instat.tasklist.busines.local.TaskLocal
import uz.instat.tasklist.busines.util.logi
import uz.instat.tasklist.framework.datasource.MainDataSource
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val mainDataSource: MainDataSource,
    private val cacheMapper: TaskCacheMapper
) : MainRepository {

    override fun saveTask(task: TaskLocal): Flow<UiState<Long>> {
        val cache = cacheMapper.mapFromLocal(task)
        return mainDataSource.saveTask(cache).map {
            object : DataStateHandler<Long, Long>(it) {
                override fun handleSuccess(data: Long): UiState.Success<Long> {
                    return UiState.Success(data)
                }
            }.getResult()
        }
    }

    override fun getAllTasks(): Flow<UiState<List<TaskLocal>>> {
        return mainDataSource.getAllTasks().map {
            object : DataStateHandler<List<TaskCache>, List<TaskLocal>>(it) {
                override fun handleSuccess(data: List<TaskCache>): UiState.Success<List<TaskLocal>> {
                    val list = cacheMapper.mapFromCacheList(data)
                    return UiState.Success(list)
                }
            }.getResult()
        }
    }

    override fun getInProgressTasks(): Flow<UiState<List<TaskLocal>>> {
        return mainDataSource.getInProgressTasks().map {
            object : DataStateHandler<List<TaskCache>, List<TaskLocal>>(it) {
                override fun handleSuccess(data: List<TaskCache>): UiState.Success<List<TaskLocal>> {
                    val list = cacheMapper.mapFromCacheList(data)
                    return UiState.Success(list)
                }
            }.getResult()
        }
    }

    override fun getPerformedTasks(): Flow<UiState<List<TaskLocal>>> {
        return mainDataSource.getPerformedTasks().map {
            object : DataStateHandler<List<TaskCache>, List<TaskLocal>>(it) {
                override fun handleSuccess(data: List<TaskCache>): UiState.Success<List<TaskLocal>> {
                    val list = cacheMapper.mapFromCacheList(data)
                    return UiState.Success(list)
                }
            }.getResult()
        }
    }

    override fun finishTask(id: Long): Flow<UiState<Unit>> {
        return mainDataSource.finishTask(id).map {
            object : DataStateHandler<Unit, Unit>(it) {
                override fun handleSuccess(data: Unit): UiState.Success<Unit> {
                    return UiState.Success(data)
                }
            }.getResult()
        }
    }

    override fun inProgressTask(id: Long): Flow<UiState<Unit>> {
        return mainDataSource.inProgressTask(id).map {
            logi("inProgressTask")
            object : DataStateHandler<Unit, Unit>(it) {
                override fun handleSuccess(data: Unit): UiState.Success<Unit> {
                    return UiState.Success(data)
                }
            }.getResult()
        }
    }

    override fun getTask(id: Long): Flow<UiState<TaskLocal>> {
        return mainDataSource.getTask(id).map {
            object : DataStateHandler<TaskCache, TaskLocal>(it) {
                override fun handleSuccess(data: TaskCache): UiState.Success<TaskLocal> {
                    return UiState.Success(cacheMapper.mapFromCache(data))
                }
            }.getResult()
        }
    }

    override fun deleteTask(id: Long): Flow<UiState<Unit>> {
        return mainDataSource.deleteTask(id).map {
            object : DataStateHandler<Unit, Unit>(it) {
                override fun handleSuccess(data: Unit): UiState.Success<Unit> {
                    return UiState.Success(data)
                }
            }.getResult()
        }

    }

}