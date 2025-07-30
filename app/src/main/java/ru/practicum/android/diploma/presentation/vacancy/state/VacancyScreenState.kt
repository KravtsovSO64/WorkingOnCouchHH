package ru.practicum.android.diploma.presentation.vacancy.state

import ru.practicum.android.diploma.domain.models.ErrorType
import ru.practicum.android.diploma.domain.models.VacancyDetail

sealed interface VacancyScreenState {
    data object Loading : VacancyScreenState
    data class Content(val data: VacancyDetail) : VacancyScreenState
    data class Error(val type: ErrorType) : VacancyScreenState
}
