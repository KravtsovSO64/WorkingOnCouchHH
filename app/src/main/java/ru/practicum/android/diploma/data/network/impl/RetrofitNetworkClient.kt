package ru.practicum.android.diploma.data.network.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.VacanciesRequest
import ru.practicum.android.diploma.data.network.impl.VacanciesRepositoryImpl
import ru.practicum.android.diploma.data.network.api.YandexVacanciesApi
import ru.practicum.android.diploma.data.network.interfaces.NetworkClient

class RetrofitNetworkClient(
    private val yandexVacanciesApi: YandexVacanciesApi
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        if (dto !is VacanciesRequest) {
            return Response().apply { resultCode =
                VacanciesRepositoryImpl.Companion.NET_BAD_REQUEST
            }
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = yandexVacanciesApi.getVacancies(
                    dto.text, dto.page
                )
                response.apply { resultCode = VacanciesRepositoryImpl.Companion.NET_SUCCESS }
            } catch (e: Exception) {
                Response().apply { resultCode = VacanciesRepositoryImpl.Companion.NET_BAD_REQUEST }
            }
        }
    }
}
