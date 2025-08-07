package ru.practicum.android.diploma.data.network.interfaces

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.domain.models.SearchResult
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.util.Resource

interface VacanciesRepository {
    fun searchVacancies(
        text: String,
        page: Int,
        area: String?,
        industry: String?,
        salary: Int?,
        onlyWithSalary: Boolean,
    ): Flow<Resource<SearchResult>>

    fun detailsVacancy(
        id: String
    ): Flow<Resource<VacancyDetail>>

    fun getIndustries(): Flow<Resource<List<FilterIndustry>>>

}
