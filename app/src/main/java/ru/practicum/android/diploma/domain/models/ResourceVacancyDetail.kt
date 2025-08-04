package ru.practicum.android.diploma.domain.models

sealed class ResourceVacancyDetail {

    data class Success(val data: VacancyDetail) : ResourceVacancyDetail()
    data class Error(val code: Int) : ResourceVacancyDetail()

}
