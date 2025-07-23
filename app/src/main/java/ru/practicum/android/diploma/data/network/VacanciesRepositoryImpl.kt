package ru.practicum.android.diploma.data.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.VacanciesRequest
import ru.practicum.android.diploma.data.dto.VacanciesResponse
import ru.practicum.android.diploma.data.dto.VacancyDto
import ru.practicum.android.diploma.domain.models.ProfessionalRole
import ru.practicum.android.diploma.domain.models.Vacancy

class VacanciesRepositoryImpl : VacanciesRepository {


    companion object{
        const val NET_SUCCESS = 200
        const val NET_BAD_REQUEST = 400
    }
    val networkClient = RetrofitNetworkClient()

    override fun searchVacancies(
        page: Int
    ): Flow<List<Vacancy>> = flow {
        val networkClientResponse = networkClient.doRequest(VacanciesRequest(page))

        when (networkClientResponse.resultCode) {
            NET_SUCCESS -> {
                emit(convertFromDto((networkClientResponse as VacanciesResponse).vacanciesList))
            }

            NET_BAD_REQUEST -> {}
            else -> {}
        }
    }

    private fun convertFromDto(listVacancyDto: List<VacancyDto>): List<Vacancy>{
        return listVacancyDto.map {
            with(it) {
                Vacancy(
                    name = name,
                    area = area?.name ?: "",
                    employer = employer?.name ?: "",
                    salaryFrom = salary?.from ?: 0,
                    salaryTo = salary?.to ?: 0,
                    schedule = schedule.name,
                    experience = experience?.name ?: "",
                    employerLogo = employer?.logos?.size90 ?: "",
                    snippetTitle = snippet.requirement ?: "",
                    snippetDescription = snippet.requirement ?: "",
                    professionalRoles = professionalRoles?.map {
                        ProfessionalRole(it.id, it.name)
                    } ?: listOf(),
                    employment = employment.name,
                )
            }
        }
    }
}
