package ru.practicum.android.diploma.domain.models

sealed class ResourceVacancy {
    data class Success(val data: List<Vacancy>, val page: Int, val pages: Int, val total: Int) : ResourceVacancy()
    data class Error(val code: Int) : ResourceVacancy()

}
