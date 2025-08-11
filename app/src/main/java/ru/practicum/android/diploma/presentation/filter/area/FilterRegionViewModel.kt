package ru.practicum.android.diploma.presentation.filter.area

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.vacancy.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.ErrorType
import ru.practicum.android.diploma.domain.models.FilterArea
import ru.practicum.android.diploma.domain.models.ResourceAreas
import ru.practicum.android.diploma.presentation.filter.area.state.FilterRegionState

@Suppress("IMPLICIT_CAST_TO_ANY")
class FilterRegionViewModel(
   private val interactor: VacanciesInteractor
): ViewModel() {

    private val _regions = MutableLiveData<List<FilterArea>>()
    val regions: LiveData<List<FilterArea>> = _regions

    private val _stateScreen = MutableLiveData<FilterRegionState>()
    val stateScreen: LiveData<FilterRegionState> = _stateScreen

    fun getListArea(country: Int) {
        _stateScreen.postValue(FilterRegionState.Loading)

        if (country < 0) {
            viewModelScope.launch {
                interactor.getAreas()
                    .collect { resource ->
                        when (resource) {
                            is ResourceAreas.Success -> {
                                val areas = resource.data.flatMap { it.areas.orEmpty() }
                                _regions.postValue(areas)
                                _stateScreen.postValue(FilterRegionState.Content(areas))
                            }
                            is ResourceAreas.Error -> {
                                _stateScreen.postValue(FilterRegionState.Error(ErrorType.EMPTY))
                            }
                        }
                    }
            }
        } else {
            viewModelScope.launch {
                interactor.getAreas()
                    .collect { resource ->
                        when (resource) {
                            is ResourceAreas.Success -> {
                                val areas = resource.data
                                    .firstOrNull { it.id == country }
                                    ?.areas
                                    ?: emptyList()
                                _regions.postValue(areas)
                                _stateScreen.postValue(FilterRegionState.Content(areas))
                            }
                            is ResourceAreas.Error -> {
                                _stateScreen.postValue(FilterRegionState.Error(ErrorType.EMPTY))
                            }
                        }
                    }
            }
        }
    }

    fun filterRegions(searchQuery: String) {
        val currentList = _regions.value ?: return

        val filteredList = if (searchQuery.isBlank()) {
            currentList
        } else {
            currentList.filter { filterArea ->
                filterArea.name.contains(searchQuery, ignoreCase = true)
            }
        }

        _regions.value = filteredList
    }
}


