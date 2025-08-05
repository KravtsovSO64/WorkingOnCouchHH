package ru.practicum.android.diploma.presentation.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.filter.FilterCacheInteractor
import ru.practicum.android.diploma.domain.filter.FilterInteractor
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.FilterSalary
import ru.practicum.android.diploma.presentation.filter.state.FilterSettingsState

class FilterSettingsViewModel(private val filterInteractor: FilterInteractor, private val filterCacheInteractor: FilterCacheInteractor,
) : ViewModel() {
    private var screenStateLiveData = MutableLiveData<FilterSettingsState>()
    private var applyButtonLiveData = MutableLiveData<Boolean>()
    private var resetButtonLiveData = MutableLiveData<Boolean>()
    val salaryTextState = MutableLiveData("")

    private var filtersAppliedEvent = MutableLiveData<Boolean>()
    private var currentSalary: Int? = null
    private var dontShowWithoutSalary: Boolean? = false

    init{
        viewModelScope.launch {
            filterCacheInteractor.createCache()
            val filterSettings = if (filterInteractor.getFilter() == null) {
                filterInteractor.initializeEmptyFilter()
                filterInteractor.getFilter()
            } else {
                filterInteractor.getFilter()
            }
            currentSalary = filterSettings?.salary?.salary
            dontShowWithoutSalary = filterSettings?.salary?.onlyWithSalary
            screenStateLiveData.postValue(FilterSettingsState(filterSettings ?: Filter()))
            applyButtonLiveData.postValue(false)
            resetButtonLiveData.postValue(filterCacheInteractor.isCachedFilterEmpty())
        }
    }

    fun onSalaryTextChanged(
        p0: CharSequence?,
    ) {
        salaryTextState.postValue(p0.toString())
        currentSalary = p0?.toString()?.toIntOrNull()

    }

    fun observeScreenStateLiveData(): LiveData<FilterSettingsState> = screenStateLiveData
    fun observeApplyButtonLiveData(): LiveData<Boolean> = applyButtonLiveData
    fun observeResetButtonLiveData(): LiveData<Boolean> = resetButtonLiveData
    fun observeSalaryTextLiveData(): LiveData<String> = salaryTextState
    fun observeFiltersAddedEvent(): LiveData<Boolean> = filtersAppliedEvent
    fun updateFilterData() {
        viewModelScope.launch {
            val cachedSettings = filterCacheInteractor.getCache()
            screenStateLiveData.postValue(FilterSettingsState(cachedSettings ?: Filter()))
            applyButtonLiveData.postValue(filterCacheInteractor.isCachedFilterChanged())
            resetButtonLiveData.postValue(filterCacheInteractor.isCachedFilterEmpty())
        }
    }

    fun clearSalary() {
        salaryTextState.postValue("")
        filterCacheInteractor.writeCache(
            Filter(salary = null)
        )
        applyButtonLiveData.postValue(filterCacheInteractor.isCachedFilterChanged())
        resetButtonLiveData.postValue(filterCacheInteractor.isCachedFilterEmpty())
    }

    fun clearIndustry() {
        viewModelScope.launch {
            filterCacheInteractor.writeCache(Filter(industry = null))
            val savedFilter = filterCacheInteractor.getCache()
            screenStateLiveData.postValue(FilterSettingsState(savedFilter ?: Filter()))
        }
    }

    fun clearRegion() {

    }

    fun onActionDone() {
        viewModelScope.launch {
            filterCacheInteractor.commitCache()
//            filterCacheInteractor.writeCache(
//                    Filter(salary = FilterSalary(currentSalary!!, dontShowWithoutSalary!!))
//            )
            applyButtonLiveData.postValue(filterCacheInteractor.isCachedFilterChanged())
            resetButtonLiveData.postValue(filterCacheInteractor.isCachedFilterEmpty())
        }
    }

    fun applyFilters(bool: Boolean) {
        viewModelScope.launch {
            filterCacheInteractor.commitCache()
            filtersAppliedEvent.postValue(bool)
            filterInteractor.saveFilterApplicationSetting(bool)
        }
    }
    fun resetFilters() {
        viewModelScope.launch {
            filterInteractor.deleteFilters()
            filterCacheInteractor.invalidateCache()
            filterInteractor.initializeEmptyFilter()
            val filterSettings = filterInteractor.getFilter()
            filterCacheInteractor.createCache()
            screenStateLiveData.postValue(FilterSettingsState(filterSettings ?: Filter()))
            applyButtonLiveData.postValue(false)
            resetButtonLiveData.postValue(filterCacheInteractor.isCachedFilterEmpty())
        }
    }

    fun addSalaryCheckFilter(checked: Boolean) {}


}
