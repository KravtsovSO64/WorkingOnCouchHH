package ru.practicum.android.diploma.data.network.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.dto.AreasRequest
import ru.practicum.android.diploma.data.dto.AreasResponse
import ru.practicum.android.diploma.data.dto.IndustriesRequest
import ru.practicum.android.diploma.data.dto.IndustriesResponse
import ru.practicum.android.diploma.data.dto.VacanciesRequest
import ru.practicum.android.diploma.data.dto.VacanciesResponse
import ru.practicum.android.diploma.data.dto.VacancyDetailRequest
import ru.practicum.android.diploma.data.dto.vacancy.VacancyDetailDto
import ru.practicum.android.diploma.data.dto.vacancy.VacancyDto
import ru.practicum.android.diploma.data.dto.vacancy.elements.FilterAreaDto
import ru.practicum.android.diploma.data.network.interfaces.NetworkClient
import ru.practicum.android.diploma.data.network.interfaces.VacanciesRepository
import ru.practicum.android.diploma.domain.models.FilterArea
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.domain.models.SearchResult
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.util.Resource
import kotlin.Int

class VacanciesRepositoryImpl(
    private val networkClient: NetworkClient
) : VacanciesRepository {
    companion object {
        const val NET_SUCCESS = 200
        const val NET_BAD_REQUEST = 400
        const val UNKNW_HOST = -1
        const val UNAUTHORIZED = 401
        const val REQ_TIMEOUT = 408
    }

    override fun searchVacancies(
        text: String,
        page: Int,
        area: String?,
        industry: String?,
        salary: Int?,
        onlyWithSalary: Boolean,
    ): Flow<Resource<SearchResult>> = flow {
        val networkClientResponse = networkClient.doRequest(
            VacanciesRequest(text, page, area, industry, salary, onlyWithSalary)
        )

        when (networkClientResponse.resultCode) {
            NET_SUCCESS -> {
                emit(
                    Resource.Success(
                        SearchResult(
                            found = (networkClientResponse as VacanciesResponse).found,
                            pages = (networkClientResponse as VacanciesResponse).pages,
                            page = (networkClientResponse as VacanciesResponse).page,
                            items = convertFromDto((networkClientResponse as VacanciesResponse).items)
                        )
                    )
                )
            }

            UNKNW_HOST -> {
                emit(Resource.Error(UNKNW_HOST, "Проверьте подключение к интернету"))
            }

            REQ_TIMEOUT -> {
                emit(Resource.Error(REQ_TIMEOUT, "Время подключение к серверу истекло"))
            }

            else -> {
                emit(Resource.Error(NET_BAD_REQUEST, "Ошибка сервера"))
            }
        }
    }

    override fun detailsVacancy(id: String): Flow<Resource<VacancyDetail>> = flow {
        val networkClientResponse = networkClient.doRequest(
            VacancyDetailRequest(id)
        )

        when (networkClientResponse.resultCode) {
            NET_SUCCESS -> {
                emit(Resource.Success(convertToVacancyDetail(networkClientResponse as VacancyDetailDto)))
            }

            UNKNW_HOST -> {
                emit(Resource.Error(UNKNW_HOST, "Проверьте подключение к интернету"))
            }

            REQ_TIMEOUT -> {
                emit(Resource.Error(REQ_TIMEOUT, "Время подключение к серверу истекло"))
            }

            else -> {
                emit(Resource.Error(NET_BAD_REQUEST, "Ошибка сервера"))
            }
        }

    }

    override fun getIndustries(): Flow<Resource<List<FilterIndustry>>> = flow {
        val networkClientResponse = networkClient.doRequest(
            IndustriesRequest()
        )

        when (networkClientResponse.resultCode) {
            NET_SUCCESS -> {
                emit(Resource.Success(convertFilterIndustry(networkClientResponse as IndustriesResponse)))
            }

            UNKNW_HOST -> {
                emit(Resource.Error(UNKNW_HOST, "Проверьте подключение к интернету"))
            }

            REQ_TIMEOUT -> {
                emit(Resource.Error(REQ_TIMEOUT, "Время подключение к серверу истекло"))
            }

            else -> {
                emit(Resource.Error(NET_BAD_REQUEST, "Ошибка сервера"))
            }
        }
    }

    override fun getAreas(): Flow<Resource<List<FilterArea>>> = flow {
        val networkClientResponse = networkClient.doRequest(
            AreasRequest()
        )

        when (networkClientResponse.resultCode) {
            NET_SUCCESS -> {
                emit(
                    Resource.Success(
                        convertFilterArea(
                            (networkClientResponse as AreasResponse).areas
                        )
                    )
                )
            }

            UNKNW_HOST -> {
                emit(Resource.Error(UNKNW_HOST, "Проверьте подключение к интернету"))
            }

            REQ_TIMEOUT -> {
                emit(Resource.Error(REQ_TIMEOUT, "Время подключение к серверу истекло"))
            }

            else -> {
                emit(Resource.Error(NET_BAD_REQUEST, "Ошибка сервера"))
            }
        }
    }

    private fun convertFilterArea(listFilterAreaDto: List<FilterAreaDto>?): List<FilterArea> {
        return if (listFilterAreaDto.isNullOrEmpty()) {
            listOf<FilterArea>()
        } else {
            listFilterAreaDto.map {
                FilterArea(
                    id = it.id,
                    parentId = it.parentId,
                    name = it.name,
                    areas = convertFilterArea(it.areas)
                )
            }
        }
    }

    private fun convertFilterIndustry(industriesResponse: IndustriesResponse): List<FilterIndustry> {
        return industriesResponse.industries.map {
            FilterIndustry(
                id = it.id,
                name = it.name
            )
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
                salaryFrom = salary?.from ?: 0,
                salaryTo = salary?.to ?: 0,
                salaryCurrency = salary?.currency ?: "",
                addressCity = address?.city.orEmpty(),
                employerName = employer.name,
                employerLogo = employer.logo,
                area = FilterArea(
                    area.id,
                    area.parentId,
                    area.name,
                    convertFilterArea(area.areas)
                ),
                industry = FilterIndustry(
                    industry.id,
                    industry.name,
                ),
            )
        }
    }

    private fun convertToVacancyDetail(vacancyDetailDto: VacancyDetailDto): VacancyDetail {
        if (vacancyDetailDto == null) return VacancyDetail.empty()
        return with(vacancyDetailDto) {
            VacancyDetail(
                id = id,
                name = name,
                description = description,
                salaryFrom = salary?.from ?: 0,
                salaryTo = salary?.to ?: 0,
                salaryCurrency = salary?.currency ?: "",
                addressCity = address?.city.orEmpty(),
                addressStreet = address?.street.orEmpty(),
                addressBuilding = address?.building.orEmpty(),
                experience = experience.name,
                schedule = schedule.name,
                employment = employment.name,
                contactsName = contacts?.name.orEmpty(),
                contactsEmail = contacts?.email.orEmpty(),
                contactsPhone = listOf(),
                employerName = employer.name,
                employerLogo = employer.logo,
                area = FilterArea(
                    area.id,
                    area.parentId,
                    area.name,
                    convertFilterArea(area.areas)
                ),
                skills = skills.orEmpty(),
                url = url,
                industry = FilterIndustry(
                    industry.id,
                    industry.name,
                ),
            )
        }
    }
}
