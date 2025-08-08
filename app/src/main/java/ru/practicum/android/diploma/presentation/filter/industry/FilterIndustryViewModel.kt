package ru.practicum.android.diploma.presentation.filter.industry

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.filter.FilterCacheInteractor
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.domain.models.ResourceIndustries
import ru.practicum.android.diploma.presentation.filter.industry.state.FilterIndustryListState
import ru.practicum.android.diploma.presentation.filter.industry.state.FilterIndustryState

class FilterIndustryViewModel(
    private val interactor: VacanciesInteractor,
    private val filterCacheInteractor: FilterCacheInteractor
) : ViewModel() {
    private var selected: FilterIndustry? = null
    private var savedFilterSetting: Filter? = null

    private val state = MutableLiveData(
        FilterIndustryState(
            "",
            selected != null
        )
    )

    init {
        load()
    }


    fun observeIndustryState(): LiveData<FilterIndustryState> = state

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
            interactor.getIndustries()
                .collect {
                    items.postValue(
                        when (it) {
                            is ResourceIndustries.Success -> {
                                if (it.data.isEmpty()) {
                                    FilterIndustryListState.Error
                                } else {
                                    FilterIndustryListState.Content(it.data, selected)
                                }
                            }

                            is ResourceIndustries.Error -> {
                                FilterIndustryListState.Error
                            }
                        }
                    )
                }
        }
    }

    fun onChecked(industry: FilterIndustry?) {
        selected = industry
        state.value = state.value?.copy(isSaveEnable = industry != null)
        val setting = if (industry != null) {
            Filter(
                industry = FilterIndustry(
                    industry.id,
                    industry.name
                )

            )
        } else {
            Filter()
        }
        filterCacheInteractor.writeCache(setting,
            setRegion = false,
            setSalary = false,
            setIndustry = true
        )
    }

}
