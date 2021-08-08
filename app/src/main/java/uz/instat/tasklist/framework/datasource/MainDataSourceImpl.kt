package uz.instat.tasklist.framework.datasource

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uz.instat.tasklist.busines.cache.TaskDatabase
import uz.instat.tasklist.busines.cache.data.TaskCache
import uz.instat.tasklist.busines.interactors.DataState
import uz.instat.tasklist.busines.util.loge
import javax.inject.Inject

class MainDataSourceImpl @Inject constructor(
    private val db: TaskDatabase
) : MainDataSource {
    private val dao = db.todoDao()

    override fun saveTask(task: TaskCache): Flow<DataState<Long>> {
        return flow {
            try {
                emit(DataState.data(dao.insertTask(task)))
            } catch (e: Exception) {
                loge(e.message)
                emit(DataState.error<Long>(-1))
            }
        }
    }

    override fun updateTask(task: TaskCache): Flow<DataState<Unit>> {
        return flow {
            try {
                emit(DataState.data(dao.updateTask(task)))
            } catch (e: Exception) {
                loge(e.message)
                emit(DataState.error<Unit>(-1))
            }
        }
    }

    override fun getAllTasks(): Flow<DataState<List<TaskCache>>> {
        return flow {
            try {
                emit(DataState.data(dao.getAllTasks()))
            } catch (e: Exception) {
                loge(e.message)
                emit(DataState.error<List<TaskCache>>(-1))
            }
        }
    }


    override fun getInProgressTasks(): Flow<DataState<List<TaskCache>>> {
        return flow {
            try {
                emit(DataState.data(dao.getInProgressTasks()))
            } catch (e: Exception) {
                loge(e.message)
                emit(DataState.error<List<TaskCache>>(-1))
            }
        }
    }

    override fun getPerformedTasks(): Flow<DataState<List<TaskCache>>> {
        return flow {
            try {
                emit(DataState.data(dao.getFinishTasks()))
            } catch (e: Exception) {
                loge(e.message)
                emit(DataState.error<List<TaskCache>>(-1))
            }
        }
    }

    override fun finishTask(id: Long): Flow<DataState<Unit>> {
        return flow {
            try {
                emit(DataState.data(dao.finishTask(id)))
            } catch (e: Exception) {
                loge(e.message)
                emit(DataState.error<Unit>(-1))
            }
        }
    }

    override fun inProgressTask(id: Long): Flow<DataState<Unit>> {
        return flow {
            try {
                emit(DataState.data(dao.inProgressTask(id)))
            } catch (e: Exception) {
                loge(e.message)
                emit(DataState.error<Unit>(-1))
            }
        }
    }

    override fun getTask(id: Long): Flow<DataState<TaskCache>> {
        return flow {
            try {
                emit(DataState.data(dao.getTask(id)))
            } catch (e: Exception) {
                loge(e.message)
                emit(DataState.error<TaskCache>(-1))
            }
        }
    }

    override fun deleteTask(id: Long): Flow<DataState<Unit>> {
        return flow {
            try {
                emit(DataState.data(dao.deleteTask(id)))
            } catch (e: Exception) {
                loge(e.message)
                emit(DataState.error<Unit>(-1))
            }
        }
    }
}