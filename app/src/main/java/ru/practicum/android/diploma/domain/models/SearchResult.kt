package ru.practicum.android.diploma.domain.models


data class SearchResult(
    val found: Int, val pages: Int, val page: Int, val items: List<Vacancy>
)
