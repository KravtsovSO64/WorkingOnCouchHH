package ru.practicum.android.diploma.presentation.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.presentation.filter.state.FilterSettingsState

class FilterSettingsViewModel : ViewModel() {
    private var screenStateLiveData = MutableLiveData<FilterSettingsState>()
    private var applyButtonLiveData = MutableLiveData<Boolean>()
    private var resetButtonLiveData = MutableLiveData<Boolean>()
    val salaryTextState = MutableLiveData("")

    private var filtersAppliedEvent = MutableLiveData<Boolean>()
    private var currentSalary: Int? = null
    private var dontShowWithoutSalary: Boolean? = null

    fun observeScreenStateLiveData(): LiveData<FilterSettingsState> = screenStateLiveData
    fun observeApplyButtonLiveData(): LiveData<Boolean> = applyButtonLiveData
    fun observeResetButtonLiveData(): LiveData<Boolean> = resetButtonLiveData
    fun observeSalaryTextLiveData(): LiveData<String> = salaryTextState
    fun observeFiltersAddedEvent(): LiveData<Boolean> = filtersAppliedEvent
    fun updateFilterData() {

    }

    fun clearSalary() {

    }

    fun clearIndustry() {

    }

    fun clearRegion() {

    }

    fun onActionDone() {

    }

    fun applyFilters(bool: Boolean) {}
    fun resetFilters() {

    }

    fun addSalaryCheckFilter(checked: Boolean) {}


}
