package ru.practicum.android.diploma.data.network

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.data.dto.VacanciesRequest
import ru.practicum.android.diploma.data.dto.VacancyResponse
import ru.practicum.android.diploma.data.dto.vacancy.VacancyDto
import ru.practicum.android.diploma.domain.models.Vacancy
import kotlin.Int

class VacanciesRepositoryImpl(
    private val networkClient: NetworkClient
) : VacanciesRepository {
    companion object {
        const val NET_SUCCESS = 200
        const val NET_BAD_REQUEST = 400
    }


    override fun searchVacancies(
        page: Int,
    ): Flow<List<Vacancy>> = flow {
        val networkClientResponse = networkClient.doRequest(
            VacanciesRequest(
                //BuildConfig.HH_ACCESS_TOKEN,
                "",
                page
            )
        )

        when (networkClientResponse.resultCode) {
            NET_SUCCESS -> {
                emit(convertFromDto((networkClientResponse as VacancyResponse).items))
            }

            NET_BAD_REQUEST -> {}
            else -> {}
        }
    }

    private fun convertFromDto(listVacancyDto: Array<VacancyDto>): List<Vacancy> {
        return listVacancyDto.map {
            convertToVacancy(it)
        }
    }

    private fun convertToVacancy(vacancyDto: VacancyDto): Vacancy {
        return with(vacancyDto) {
            Vacancy(
                id = id,
                name = name,
                description = description,
                salaryFrom = salary?.from ?: 0,
                salaryTo = salary?.to ?: 0,
                salaryCurrency = salary?.currency ?: "",
                addressCity = address.city,
                addressStreet = address.street,
                addressBuilding = address.building,
                experience = experience.name,
                schedule = schedule.name,
                employment = employment.name,
                contactsName = contacts.name,
                contactsEmail = contacts.email,
                contactsPhone = listOf(),
                employerName = employer.name,
                employerLogo = employer.logo,
                //area: FilterAreaDto //id, parentId, name, areas
                skills = skills,
                url = "url",
                //industry: FilterIndustryDto //id, name
            )
        }
    }
}
