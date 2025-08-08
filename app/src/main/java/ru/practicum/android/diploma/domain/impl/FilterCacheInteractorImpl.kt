package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.filter.FilterCacheInteractor
import ru.practicum.android.diploma.domain.filter.FilterCacheRepository
import ru.practicum.android.diploma.domain.models.Filter

class FilterCacheInteractorImpl(private val filterCacheRepository: FilterCacheRepository) : FilterCacheInteractor {
    override fun createCache() {
        filterCacheRepository.createCache()
    }

    override fun getCache(): Filter? {
        return filterCacheRepository.getCache()
    }

    override fun commitCache() {
        filterCacheRepository.commitCache()
    }

    override fun isCachedFilterChanged(): Boolean {
        return filterCacheRepository.isCachedFilterChanged()
    }

    override fun isCachedFilterEmpty(): Boolean {
        return filterCacheRepository.isCachedFilterEmpty()
    }

    override fun writeCache(
        setting: Filter,
        setRegion: Boolean,
        setSalary: Boolean,
        setIndustry: Boolean
    ) {
        filterCacheRepository.writeCache(setting, setRegion, setSalary, setIndustry)
    }


    override fun invalidateCache() {
        filterCacheRepository.invalidateCache()
    }
}
