package ru.practicum.android.diploma.domain.models

data class FilterArea(
    val id: Int,
    val parentId: Int,
    val name: String,
    val areas: List<String>,
)
