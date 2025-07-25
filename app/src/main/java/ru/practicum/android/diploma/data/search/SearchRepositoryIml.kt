package ru.practicum.android.diploma.data.search

import ru.practicum.android.diploma.domain.models.ResourceVacancy
import ru.practicum.android.diploma.domain.search.SearchRepository

class SearchRepositoryIml() : SearchRepository {

    override suspend fun search(
        text: String,
        page: Int,
        perPage: Int
    ): ResourceVacancy {
        return ResourceVacancy.Success(emptyList(), 0, 0, 0)
    }
}
