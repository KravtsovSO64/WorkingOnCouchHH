package ru.practicum.android.diploma.domain.filter

import ru.practicum.android.diploma.domain.models.Filter

interface FilterRepository {
    fun initializeEmptyFilter()
    fun saveSetting(setting: Filter)
    fun getFilter(): Filter?
    fun isFilterPresent(): Boolean
    fun deleteFilter()
    fun saveFilterApplicationSetting(apply: Boolean)
    fun readFilterApplicationSetting(): Boolean
}
