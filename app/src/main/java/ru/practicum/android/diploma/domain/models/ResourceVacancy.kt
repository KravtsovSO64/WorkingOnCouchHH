package ru.practicum.android.diploma.domain.models

sealed class ResourceVacancy {

    data class Success(val data: SearchResult) : ResourceVacancy()
    data class Error(val code: Int) : ResourceVacancy()

}
