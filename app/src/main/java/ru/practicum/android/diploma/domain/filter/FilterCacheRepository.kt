package ru.practicum.android.diploma.domain.filter

import ru.practicum.android.diploma.domain.models.Filter

interface FilterCacheRepository {
    fun createCache()
    fun getCache(): Filter?
    fun commitCache()
    fun isCachedFilterChanged(): Boolean
    fun isCachedFilterEmpty(): Boolean
    fun writeCache(setting: Filter)
    fun invalidateCache()
}
