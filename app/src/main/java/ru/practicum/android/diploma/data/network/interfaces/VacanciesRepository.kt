package ru.practicum.android.diploma.data.network.interfaces

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.util.Resource

interface VacanciesRepository {
    fun searchVacancies(
        text: String,
        page: Int,
    ): Flow<Resource<List<Vacancy>>>
}
