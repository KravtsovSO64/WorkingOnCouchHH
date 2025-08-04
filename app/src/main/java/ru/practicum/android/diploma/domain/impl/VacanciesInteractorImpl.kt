package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.practicum.android.diploma.data.network.interfaces.VacanciesRepository
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.ErrorCode
import ru.practicum.android.diploma.domain.models.ResourceVacancy
import ru.practicum.android.diploma.domain.models.ResourceVacancyDetail
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.util.Resource

class VacanciesInteractorImpl(
    private val vacanciesRepository: VacanciesRepository
) : VacanciesInteractor {

    override fun searchVacancies(text: String, page: Int): Flow<ResourceVacancy> {
        return vacanciesRepository.searchVacancies(text, page).map { result ->

            when (result) {
                is Resource.Success -> {
                    result.data?.let {
                        ResourceVacancy.Success(it)
                    } ?: ResourceVacancy.Success(emptyList())
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
                        VacancyDetail.empty())
                }

                is Resource.Error -> {
                    result.code?.let {
                        ResourceVacancyDetail.Error(it)
                    } ?: ResourceVacancyDetail.Error(ErrorCode.NOT_FOUND)
                }
            }
        }
    }
}
