package ru.practicum.android.diploma.domain.api

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.ResourceVacancy
import ru.practicum.android.diploma.domain.models.ResourceVacancyDetail
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetail

interface VacanciesInteractor {
    fun searchVacancies(
        text: String,
        page: Int,
    ): Flow<ResourceVacancy>

    fun detailsVacancy(id: String): Flow<ResourceVacancyDetail>
}
