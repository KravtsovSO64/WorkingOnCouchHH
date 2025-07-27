package ru.practicum.android.diploma.domain.models

sealed class ResourceVacancy {
    data class Success(val data: List<Vacancy>) : ResourceVacancy()
    data class Error(val code: Int) : ResourceVacancy()

}
