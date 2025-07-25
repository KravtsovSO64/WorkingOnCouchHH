package ru.practicum.android.diploma.domain.search

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.ResourceVacancy

interface SearchInteractor {
    suspend fun search(text: String, page: Int): Flow<ResourceVacancy>
}
