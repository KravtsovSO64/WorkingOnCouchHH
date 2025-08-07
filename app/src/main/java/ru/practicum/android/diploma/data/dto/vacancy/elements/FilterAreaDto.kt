package ru.practicum.android.diploma.data.dto.vacancy.elements

data class FilterAreaDto(
    val id: Int,
    val parentId: Int?,
    val name: String,
    val areas: List<FilterAreaDto>?,
)
