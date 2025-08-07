package ru.practicum.android.diploma.data.network.impl

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import ru.practicum.android.diploma.data.dto.IndustriesRequest
import ru.practicum.android.diploma.data.dto.IndustriesResponse
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.VacanciesRequest
import ru.practicum.android.diploma.data.dto.VacancyDetailRequest
import ru.practicum.android.diploma.data.dto.vacancy.elements.ElementDto
import ru.practicum.android.diploma.data.network.api.YandexVacanciesApi
import ru.practicum.android.diploma.data.network.impl.VacanciesRepositoryImpl.Companion.REQ_TIMEOUT
import ru.practicum.android.diploma.data.network.impl.VacanciesRepositoryImpl.Companion.UNAUTHORIZED
import ru.practicum.android.diploma.data.network.impl.VacanciesRepositoryImpl.Companion.UNKNW_HOST
import ru.practicum.android.diploma.data.network.interfaces.NetworkClient
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RetrofitNetworkClient(
    private val yandexVacanciesApi: YandexVacanciesApi
) : NetworkClient {

    override suspend fun doRequest(dto: Any): Response {
        return try {
            withContext(Dispatchers.IO) {
                when (dto) {
                    is VacanciesRequest ->
                        yandexVacanciesApi.getVacancies(
                            parseOptions(dto)
                        ).apply { resultCode = VacanciesRepositoryImpl.Companion.NET_SUCCESS }

                    is VacancyDetailRequest ->
                        yandexVacanciesApi.getVacancyDetails(
                            dto.id,
                        ).apply { resultCode = VacanciesRepositoryImpl.Companion.NET_SUCCESS }

                    is IndustriesRequest ->
                        convertRawResponse(yandexVacanciesApi.getIndustries())

                    else -> badRequest()
                }
            }
        } catch (error: IOException) {
            errorHandler(error, UNKNW_HOST)
            Response().apply { resultCode = VacanciesRepositoryImpl.Companion.UNKNW_HOST }
        } catch (error: SocketTimeoutException) {
            errorHandler(error, REQ_TIMEOUT)
            Response().apply { resultCode = VacanciesRepositoryImpl.Companion.REQ_TIMEOUT }
        } catch (error: HttpException) {
            errorHandler(error, UNAUTHORIZED)
            Response().apply { resultCode = VacanciesRepositoryImpl.Companion.UNAUTHORIZED }
        }
    }

    private fun parseOptions(dto: VacanciesRequest): Map<String, String> {
        val options: HashMap<String, String> = HashMap()

        options["text"] = dto.text
        options["page"] = dto.page.toString()
        if (dto.area != null){
            options["area"] = dto.area
        }
        if (dto.industry != null){
            options["industry"] = dto.industry
        }
        if (dto.salary != null){
            options["salary"] = dto.salary.toString()
        }
        if (dto.onlyWithSalary) {
            options["only_with_salary"] = true.toString()
        }

        Log.i("OPTIONS", options.toString())
        return options
    }

    private fun convertRawResponse(rawList: List<ElementDto>): IndustriesResponse {
        return IndustriesResponse().apply {
            resultCode = VacanciesRepositoryImpl.Companion.NET_SUCCESS
            industries = rawList
        }
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
