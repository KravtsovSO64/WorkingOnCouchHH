package ru.practicum.android.diploma.presentation.search.state

import ru.practicum.android.diploma.domain.models.ErrorType
import ru.practicum.android.diploma.domain.models.Vacancy

sealed interface SearchState {
    data object Start : SearchState
    data class Content(val data: List<Vacancy>, val paging: Boolean) : SearchState
    data object Loading : SearchState
    data object PageLoading : SearchState
    data class Error(val type: ErrorType) : SearchState
}
