package ru.practicum.android.diploma.presentation.filter.industry.state

import ru.practicum.android.diploma.domain.models.FilterIndustry

sealed interface FilterIndustryListState {
    data class Content(
        val industries: List<FilterIndustry>,
        val current: FilterIndustry?
    ) : FilterIndustryListState

    data object Error : FilterIndustryListState
}
