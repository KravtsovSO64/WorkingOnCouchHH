package ru.practicum.android.diploma.data.filter

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import ru.practicum.android.diploma.domain.filter.FilterRepository
import ru.practicum.android.diploma.domain.models.Filter

class FilterRepositoryImpl(
    private val sharedPreference: SharedPreferences,
    private val gson: Gson,
) : FilterRepository {
    override fun initializeEmptyFilter() {
        saveToStorage(Filter())
    }

    override fun saveSetting(setting: Filter) {
        val filter = getFromStorage() ?: Filter()

        updateSetting(
            filter,
            setting
        )
        saveToStorage(filter)

    }

    override fun getFilter(): Filter? {
        return getFromStorage()
    }

    override fun isFilterPresent(): Boolean {
        val filter = getFromStorage() ?: return false
        val salaryFilled = filter.salary?.salary ?: 0 != 0
        val withSalaryFilled = filter.salary?.onlyWithSalary == true
        val regionFilled = filter.area?.region?.id != 0
        val industryFilled = !filter.industry?.id.isNullOrEmpty()
        val countryFilled = filter.area?.country?.id != 0
        return salaryFilled or withSalaryFilled or regionFilled or industryFilled or countryFilled
    }

    override fun deleteFilter() {
        sharedPreference.edit { clear() }
    }

    override fun saveFilterApplicationSetting(apply: Boolean) {
        sharedPreference.edit {
            putBoolean(
                FILTER_APPLY_KEY,
                apply
            )
        }
    }

    override fun readFilterApplicationSetting(): Boolean {
        return sharedPreference.getBoolean(
            FILTER_APPLY_KEY,
            false
        )
    }

    private fun updateSetting(
        filter: Filter,
        setting: Filter
    ) {
        if (setting.salary != null) {
            filter.salary = setting.salary
        } else if (setting.area != null) {
            filter.area = setting.area
        } else if (setting.industry != null) {
            filter.industry = setting.industry
        }
    }

    private fun saveToStorage(filter: Filter) {
        sharedPreference.edit {
            putString(
                FILTER_KEY,
                gson.toJson(filter)
            )
        }
    }

    private fun getFromStorage(): Filter? {
        val filterAsJson = sharedPreference.getString(
            FILTER_KEY,
            null
        )

        return if (filterAsJson != null) {
            gson.fromJson(
                filterAsJson,
                Filter::class.java
            )
        } else {
            null
        }
    }

    companion object {
        const val FILTER_KEY = "FILTER"
        const val FILTER_APPLY_KEY = "FILTER_APPLY_KEY"
    }
}
