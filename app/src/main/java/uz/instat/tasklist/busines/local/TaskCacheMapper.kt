package uz.instat.tasklist.busines.local

import uz.instat.tasklist.busines.cache.CacheMapper
import uz.instat.tasklist.busines.cache.data.TaskCache
import javax.inject.Inject

class TaskCacheMapper @Inject constructor() : CacheMapper<TaskCache, TaskLocal>() {
    override fun mapFromLocal(local: TaskLocal): TaskCache {
        return TaskCache(
            id = local.id,
            title = local.title,
            description = local.description,
            time = local.time,
            alarmTime = local.alarmTime,
            status = local.status
        )
    }

    override fun mapFromCache(cache: TaskCache): TaskLocal {
        return TaskLocal(
            id = cache.id,
            title = cache.title,
            description = cache.description,
            time = cache.time,
            alarmTime = cache.alarmTime,
            status = cache.status
        )
    }
}