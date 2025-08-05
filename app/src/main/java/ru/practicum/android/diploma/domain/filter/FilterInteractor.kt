package ru.practicum.android.diploma.domain.filter

import ru.practicum.android.diploma.domain.models.Filter

interface FilterInteractor {
    fun initializeEmptyFilter()
    fun saveSetting(setting: Filter)
    fun getFilter(): Filter?
    fun isFilterPresent(): Boolean
    fun deleteFilters()
    fun saveFilterApplicationSetting(apply: Boolean)
    fun readFilterApplicationSetting(): Boolean
}
