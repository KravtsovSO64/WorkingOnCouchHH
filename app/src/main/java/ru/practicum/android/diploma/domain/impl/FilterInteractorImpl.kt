package ru.practicum.android.diploma.domain.impl

import ru.practicum.android.diploma.domain.filter.FilterInteractor
import ru.practicum.android.diploma.domain.filter.FilterRepository
import ru.practicum.android.diploma.domain.models.Filter

class FilterInteractorImpl(
    private val filterRepository: FilterRepository,
) : FilterInteractor {
    override fun initializeEmptyFilter() {
        filterRepository.initializeEmptyFilter()
    }

    override fun saveSetting(setting: Filter) {
        filterRepository.saveSetting(setting)
    }

    override fun getFilter(): Filter? {
        return filterRepository.getFilter()
    }

    override fun isFilterPresent(): Boolean {
        return filterRepository.isFilterPresent()
    }

    override fun deleteFilters() {
        filterRepository.deleteFilter()
    }

    override fun saveFilterApplicationSetting(apply: Boolean) {
        filterRepository.saveFilterApplicationSetting(apply)
    }

    override fun readFilterApplicationSetting(): Boolean {
        return filterRepository.readFilterApplicationSetting()
    }
}
