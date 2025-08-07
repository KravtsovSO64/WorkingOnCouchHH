package ru.practicum.android.diploma.domain.models

import ru.practicum.android.diploma.data.dto.vacancy.VacancyDto

data class SearchResult(
    val found: Int,
    val pages: Int,
    val page: Int,
    val items: List<Vacancy>
)
