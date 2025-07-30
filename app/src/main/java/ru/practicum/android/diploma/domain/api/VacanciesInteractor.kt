package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.ResourceVacancy
import ru.practicum.android.diploma.domain.models.Vacancy

interface VacanciesInteractor {
    fun searchVacancies(
        text: String,
        page: Int,
    ): Flow<ResourceVacancy>

//    fun detailsVacancy(): Flow<VacancyDeta>
}
