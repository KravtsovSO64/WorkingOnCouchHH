package ru.practicum.android.diploma.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.VacanciesRequest
import ru.practicum.android.diploma.data.network.VacanciesRepositoryImpl.Companion.NET_BAD_REQUEST
import ru.practicum.android.diploma.data.network.VacanciesRepositoryImpl.Companion.NET_SUCCESS

class RetrofitNetworkClient : NetworkClient {

    private val hhBaseUrl = "https://api.hh.ru"

    private val retrofit = Retrofit.Builder()
        .baseUrl(hhBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(HHApi::class.java)

    override suspend fun doRequest(dto: Any): Response {
        if (dto !is VacanciesRequest) {
            return Response().apply { resultCode = NET_BAD_REQUEST }
        }

        val response = retrofit.getVacancies(dto.page)
        response.apply { resultCode = NET_SUCCESS }

        return response

    }
}
