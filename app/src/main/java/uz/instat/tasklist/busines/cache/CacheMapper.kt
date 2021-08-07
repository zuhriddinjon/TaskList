package uz.instat.tasklist.busines.cache

abstract class CacheMapper<Cache, Local> {

    abstract fun mapFromLocal(local: Local): Cache
    abstract fun mapFromCache(cache: Cache): Local
    open fun mapFromCacheList(list: List<Cache>): List<Local> {
        return ArrayList<Local>().apply {
            list.forEach {
                add(mapFromCache(it))
            }
        }
    }

    open fun mapFromLocalList(list: List<Local>): List<Cache> {
        return ArrayList<Cache>().apply {
            list.forEach {
                add(mapFromLocal(it))
            }
        }
    }
}