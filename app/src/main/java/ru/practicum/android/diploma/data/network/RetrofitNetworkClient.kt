package ru.practicum.android.diploma.data.network

import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.VacanciesRequest
import ru.practicum.android.diploma.data.network.VacanciesRepositoryImpl.Companion.NET_BAD_REQUEST
import ru.practicum.android.diploma.data.network.VacanciesRepositoryImpl.Companion.NET_SUCCESS

class RetrofitNetworkClient(
    private val yandexVacanciesApi: YandexVacanciesApi
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (dto !is VacanciesRequest) {
            return Response().apply { resultCode = NET_BAD_REQUEST }
        }

        val response = yandexVacanciesApi.getVacancies(
            //dto.token,
            "UX",
            dto.page)
        response.apply { resultCode = NET_SUCCESS }
        return response
    }
}
