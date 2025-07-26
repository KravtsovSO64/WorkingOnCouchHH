package ru.practicum.android.diploma.data.network

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.Vacancy

interface VacanciesRepository {
    fun searchVacancies(
        text: String,
        page: Int,
    ): Flow<List<Vacancy>>
}
