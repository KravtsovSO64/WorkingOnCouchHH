package ru.practicum.android.diploma.domain.models

sealed class ResourceIndustries {

    data class Success(val data: List<FilterIndustry>) : ResourceIndustries()
    data class Error(val code: Int) : ResourceIndustries()

}
