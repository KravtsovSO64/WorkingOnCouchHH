package ru.practicum.android.diploma.domain.models

sealed class ResourceAreas {
    data class Success(val data: List<FilterArea>) : ResourceAreas()
    data class Error(val code: Int) : ResourceAreas()
}
