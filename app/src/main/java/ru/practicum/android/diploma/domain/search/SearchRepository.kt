package ru.practicum.android.diploma.domain.search

import ru.practicum.android.diploma.domain.models.ResourceVacancy

interface SearchRepository {
    suspend fun search(
        text: String,
        page: Int,
        perPage: Int
    ): ResourceVacancy
}
