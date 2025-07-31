package ru.practicum.android.diploma.data.network.impl

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.VacanciesRequest
import ru.practicum.android.diploma.data.dto.VacancyDetailRequest
import ru.practicum.android.diploma.data.network.api.YandexVacanciesApi
import ru.practicum.android.diploma.data.network.impl.VacanciesRepositoryImpl.Companion.REQ_TIMEOUT
import ru.practicum.android.diploma.data.network.impl.VacanciesRepositoryImpl.Companion.UNAUTHORIZED
import ru.practicum.android.diploma.data.network.impl.VacanciesRepositoryImpl.Companion.UNKNW_HOST
import ru.practicum.android.diploma.data.network.interfaces.NetworkClient
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RetrofitNetworkClient(
    private val yandexVacanciesApi: YandexVacanciesApi
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        try {
            return withContext(Dispatchers.IO) {
                when (dto) {
                    is VacanciesRequest ->
                        yandexVacanciesApi.getVacancies(
                            dto.text,
                            dto.page,
                        ).apply { resultCode = VacanciesRepositoryImpl.Companion.NET_SUCCESS }

                    is VacancyDetailRequest ->
                        yandexVacanciesApi.getVacancyDetails(
                            dto.id,
                        ).apply { resultCode = VacanciesRepositoryImpl.Companion.NET_SUCCESS }

                    else -> badRequest()
                }
            }
        } catch (error: UnknownHostException) {
            errorHandler(error, UNKNW_HOST)
        } catch (error: SocketTimeoutException) {
            errorHandler(error, REQ_TIMEOUT)
        } catch (error: HttpException) {
            errorHandler(error, UNAUTHORIZED)
        } catch (error: ConnectException) {
            errorHandler(error, UNKNW_HOST)
        }
        return badRequest()
    }

    private fun badRequest(): Response {
        return Response().apply {
            resultCode = VacanciesRepositoryImpl.Companion.NET_BAD_REQUEST
        }
    }

    private fun errorHandler(error: Exception, code: Int): Response {
        Log.e("NetworkClient ERROR", error.message.orEmpty())
        return Response().apply {
            resultCode = code
        }
    }
}
