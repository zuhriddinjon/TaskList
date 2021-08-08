package uz.instat.tasklist.framework.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import uz.instat.tasklist.busines.enums.AlarmTypes
import uz.instat.tasklist.busines.interactors.UiState
import uz.instat.tasklist.busines.local.TaskLocal
import uz.instat.tasklist.busines.util.Event
import uz.instat.tasklist.framework.repo.MainRepository
import uz.instat.tasklist.framework.services.alarm.AlarmHelper
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val alarmHelper: AlarmHelper,
    private val repository: MainRepository
) : AndroidViewModel(application) {

    var alarmType: AlarmTypes = AlarmTypes.BEFORE_30

    private val _allTasks = MutableLiveData<UiState<List<TaskLocal>>>()
    val allTasks: LiveData<UiState<List<TaskLocal>>> = _allTasks

    fun getAllTasks() {
        _allTasks.postValue(UiState.Loading())
        viewModelScope.launch {
            repository.getAllTasks().collect {
                _allTasks.postValue(it)
            }
        }
    }

    private val _inProgressTasks = MutableLiveData<UiState<List<TaskLocal>>>()
    val inProgressTasks: LiveData<UiState<List<TaskLocal>>> = _inProgressTasks

    fun getInProgressTasks() {
        _inProgressTasks.postValue(UiState.Loading())
        viewModelScope.launch {
            repository.getInProgressTasks().collect {
                _inProgressTasks.postValue(it)
            }
        }
    }

    private val _performedTasks = MutableLiveData<UiState<List<TaskLocal>>>()
    val performedTasks: LiveData<UiState<List<TaskLocal>>> = _performedTasks

    fun getPerformedTasks() {
        _performedTasks.postValue(UiState.Loading())
        viewModelScope.launch {
            repository.getPerformedTasks().collect {
                _performedTasks.postValue(it)
            }
        }
    }

    private val _saveTaskState = MutableLiveData<Event<UiState<Long>>>()
    val saveTaskState: LiveData<Event<UiState<Long>>> = _saveTaskState

    fun saveTask(title: String, description: String, time: Long) {
        _saveTaskState.postValue(Event(UiState.Loading()))
        viewModelScope.launch {
            val taskLocal = TaskLocal(
                id = 0L,
                title = title,
                description = description,
                time = time,
                alarmTime = alarmType.value
            )
            repository.saveTask(
                taskLocal
            ).collect {
                if (it is UiState.Success && it.data != null) {
                    taskLocal.id = it.data
                    alarmHelper.setAlarm(taskLocal)
                }
                _saveTaskState.postValue(Event(it))
            }
        }
    }

    private val _updateTaskState = MutableLiveData<Event<UiState<Unit>>>()
    val updateTaskState: LiveData<Event<UiState<Unit>>> = _updateTaskState

    fun updateTask(task: TaskLocal, title: String, description: String, time: Long) {
        _updateTaskState.postValue(Event(UiState.Loading()))
        viewModelScope.launch {
            val taskLocal = TaskLocal(
                id = task.id,
                title = title,
                description = description,
                time = time,
                alarmTime = alarmType.value
            )
            repository.updateTask(
                taskLocal
            ).collect {
                removeAlarm(task)
                alarmHelper.setAlarm(taskLocal)
                _updateTaskState.postValue(Event(it))
            }
        }
    }

    private val _deleteTaskState = MutableLiveData<Event<UiState<Unit>>>()
    val deleteTaskState: LiveData<Event<UiState<Unit>>> = _deleteTaskState

    fun deleteTask(task: TaskLocal) {
        _deleteTaskState.postValue(Event(UiState.Loading()))
        viewModelScope.launch {
            repository.deleteTask(task.id).collect {
                removeAlarm(task)
                _deleteTaskState.postValue(Event(it))
            }
        }
    }

    private fun removeAlarm(task: TaskLocal) {
        alarmHelper.removeNotification(task.id, getApplication())
        alarmHelper.removeAlarm(task.time - task.alarmTime)
        alarmHelper.removeAlarm(task.time)
    }
}