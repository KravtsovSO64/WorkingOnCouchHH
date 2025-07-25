package ru.practicum.android.diploma.data.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.VacanciesRequest
import ru.practicum.android.diploma.data.dto.VacanciesResponse
import ru.practicum.android.diploma.data.dto.vacancy.VacancyDto
import ru.practicum.android.diploma.data.dto.vacancy.elements.ElementDto
import ru.practicum.android.diploma.domain.models.ProfessionalRole
import ru.practicum.android.diploma.domain.models.Vacancy

class VacanciesRepositoryImpl : VacanciesRepository {
    companion object {
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

    private fun convertFromDto(listVacancyDto: List<VacancyDto>): List<Vacancy> {
        return listVacancyDto.map {
            convertToVacancy(it)
        }
    }

    private fun convertToVacancy(vacancyDto: VacancyDto): Vacancy {
        return with(vacancyDto) {
            Vacancy(
                name = name,
                area = area?.name.orEmpty(),
                employer = employer?.name.orEmpty(),
                salaryFrom = salary?.from ?: 0,
                salaryTo = salary?.to ?: 0,
                schedule = schedule.name,
                experience = experience?.name.orEmpty(),
                employerLogo = employer?.logos?.size90.orEmpty(),
                snippetTitle = snippet.requirement.orEmpty(),
                snippetDescription = snippet.requirement.orEmpty(),
                professionalRoles = convertToListProfessionalRole(professionalRoles.orEmpty()),
                employment = employment.name,
            )
        }
    }

    private fun convertToListProfessionalRole(listDto: List<ElementDto>): List<ProfessionalRole> {
        return listDto.map {
            ProfessionalRole(id = it.id, name = it.name)
        }
    }
}
