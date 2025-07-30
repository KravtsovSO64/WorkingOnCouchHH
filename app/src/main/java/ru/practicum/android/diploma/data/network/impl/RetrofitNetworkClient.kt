package ru.practicum.android.diploma.data.network.impl

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.VacanciesRequest
import ru.practicum.android.diploma.data.dto.VacancyDetailRequest
import ru.practicum.android.diploma.data.network.api.YandexVacanciesApi
import ru.practicum.android.diploma.data.network.interfaces.NetworkClient
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RetrofitNetworkClient(
    private val yandexVacanciesApi: YandexVacanciesApi
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {

        when (dto) {
            is VacanciesRequest -> {
                return withContext(Dispatchers.IO) {
                    try {
                        val response = yandexVacanciesApi.getVacancies(
                            dto.text,
                            dto.page,
                        )
                        response.apply { resultCode = VacanciesRepositoryImpl.Companion.NET_SUCCESS }
                    } catch (e: UnknownHostException) {
                        Log.e("NetworkClient ERROR: No Connection", e.message.orEmpty())
                        Response().apply { resultCode = VacanciesRepositoryImpl.Companion.UNKNW_HOST }
                    } catch (e: SocketTimeoutException) {
                        Log.e("NetworkClient ERROR: Timeout", e.message.orEmpty())
                        Response().apply { resultCode = VacanciesRepositoryImpl.Companion.REQ_TIMEOUT }
                    } catch (e: HttpException) {
                        Log.e("NetworkClient ERROR: Token", e.message.orEmpty())
                        Response().apply { resultCode = VacanciesRepositoryImpl.Companion.UNAUTHORIZED }
                    }
                }
            }

            is VacancyDetailRequest -> {
                return withContext(Dispatchers.IO) {
                    try {
                        val response = yandexVacanciesApi.getVacancyDetails(
                            dto.id,
                        )
                        response.apply { resultCode = VacanciesRepositoryImpl.Companion.NET_SUCCESS }
                    } catch (e: UnknownHostException) {
                        Log.e("NetworkClient ERROR: No Connection", e.message.orEmpty())
                        Response().apply { resultCode = VacanciesRepositoryImpl.Companion.UNKNW_HOST }
                    } catch (e: SocketTimeoutException) {
                        Log.e("NetworkClient ERROR: Timeout", e.message.orEmpty())
                        Response().apply { resultCode = VacanciesRepositoryImpl.Companion.REQ_TIMEOUT }
                    } catch (e: HttpException) {
                        Log.e("NetworkClient ERROR: Token", e.message.orEmpty())
                        Response().apply { resultCode = VacanciesRepositoryImpl.Companion.UNAUTHORIZED }
                    }
                }
            }

            else -> {
                return Response().apply {
                    resultCode = VacanciesRepositoryImpl.Companion.NET_BAD_REQUEST
                }
            }
        }


    }
}
