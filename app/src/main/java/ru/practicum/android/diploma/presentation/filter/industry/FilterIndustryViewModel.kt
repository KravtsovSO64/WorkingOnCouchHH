package ru.practicum.android.diploma.presentation.filter.industry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.presentation.filter.industry.state.FilterIndustryListState
import ru.practicum.android.diploma.presentation.filter.industry.state.FilterIndustryState

class FilterIndustryViewModel: ViewModel() {
    private var selected: FilterIndustry? = null
    private var savedFilterSetting: Filter? = null

    private val state = MutableLiveData(
        FilterIndustryState(
            "",
            selected != null
        )
    )

    private val changesInvalidatedEvent = MutableLiveData<Boolean>()

    fun observeIndustryState(): LiveData<FilterIndustryState> = state
    fun observeChangesInvalidatedEvent(): LiveData<Boolean> = changesInvalidatedEvent

    val filterText = MutableLiveData("")
    private val items = MutableLiveData<FilterIndustryListState>()

    fun observeItems(): LiveData<FilterIndustryListState> = items

    fun onFilterTextChanged(
        p0: CharSequence?,
    ) {
        state.value = state.value?.copy(filterText = p0.toString())
    }

    fun onClearText() {
        state.postValue(state.value?.copy(filterText = ""))
        filterText.postValue("")
    }

    private fun load() {
        viewModelScope.launch {

        }
    }

    fun onChecked(industry: FilterIndustry?) {
        selected = industry
        state.value = state.value?.copy(isSaveEnable = industry != null)
        val setting = if (industry != null) {
            Filter(industry = FilterIndustry(industry.id,
                industry.name)

            )
        } else {
            Filter()
        }
    }

    fun invalidateFilterChanges() {
        viewModelScope.launch {

            changesInvalidatedEvent.postValue(true)
        }
    }

}
