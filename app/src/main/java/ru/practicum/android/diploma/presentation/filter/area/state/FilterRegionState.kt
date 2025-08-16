package ru.practicum.android.diploma.presentation.filter.area.state

import ru.practicum.android.diploma.domain.models.ErrorType
import ru.practicum.android.diploma.domain.models.FilterArea

sealed interface FilterRegionState {
    data object Loading : FilterRegionState
    data class Content(val data: List<FilterArea>) : FilterRegionState
    data class Error(val type: ErrorType) : FilterRegionState
}
