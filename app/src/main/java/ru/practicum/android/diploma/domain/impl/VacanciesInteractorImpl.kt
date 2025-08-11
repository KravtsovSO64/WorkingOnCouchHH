package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.network.interfaces.VacanciesRepository
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.ErrorCode
import ru.practicum.android.diploma.domain.models.ResourceAreas
import ru.practicum.android.diploma.domain.models.ResourceIndustries
import ru.practicum.android.diploma.domain.models.ResourceVacancy
import ru.practicum.android.diploma.domain.models.ResourceVacancyDetail
import ru.practicum.android.diploma.domain.models.SearchResult
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.util.Resource

class VacanciesInteractorImpl(
    private val vacanciesRepository: VacanciesRepository
) : VacanciesInteractor {

    override fun searchVacancies(
        text: String,
        page: Int,
        area: String?,
        industry: String?,
        salary: Int?,
        onlyWithSalary: Boolean,
    ): Flow<ResourceVacancy> {
        return vacanciesRepository.searchVacancies(
            text = text,
            page = page,
            area = area,
            industry = industry,
            salary = salary,
            onlyWithSalary = onlyWithSalary
        ).map { result ->

            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        ResourceVacancy.Success(it)
                    } ?: ResourceVacancy.Success(SearchResult(0, 0, 0, emptyList()))
                }

                is Resource.Error -> {
                    result.code?.let {
                        ResourceVacancy.Error(it)
                    } ?: ResourceVacancy.Error(ErrorCode.NOT_FOUND)
                }
            }

        }

    }

    override fun detailsVacancy(id: String): Flow<ResourceVacancyDetail> {
        return vacanciesRepository.detailsVacancy(id).map { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        ResourceVacancyDetail.Success(it)
                    } ?: ResourceVacancyDetail.Success(
                        VacancyDetail.empty()
                    )
                }

                is Resource.Error -> {
                    result.code?.let {
                        ResourceVacancyDetail.Error(it)
                    } ?: ResourceVacancyDetail.Error(ErrorCode.NOT_FOUND)
                }
            }
        }
    }

    override fun getIndustries(): Flow<ResourceIndustries> {
        return vacanciesRepository.getIndustries().map { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        ResourceIndustries.Success(it)
                    } ?: ResourceIndustries.Success(listOf())
                }

                is Resource.Error -> {
                    result.code?.let {
                        ResourceIndustries.Error(it)
                    } ?: ResourceIndustries.Error(ErrorCode.NOT_FOUND)
                }

            }
        }
    }

    override fun getAreas(): Flow<ResourceAreas> {
        return vacanciesRepository.getAreas().map { result ->
            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        ResourceAreas.Success(it)
                    } ?: ResourceAreas.Success(listOf())
                }

                is Resource.Error -> {
                    result.code?.let {
                        ResourceAreas.Error(it)
                    } ?: ResourceAreas.Error(ErrorCode.NOT_FOUND)
                }
            }
        }
    }
}
