package ru.practicum.android.diploma.data.network.api

import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap
import ru.practicum.android.diploma.data.dto.VacanciesResponse
import ru.practicum.android.diploma.data.dto.vacancy.VacancyDetailDto
import ru.practicum.android.diploma.data.dto.vacancy.elements.ElementDto

interface YandexVacanciesApi {
    @GET("vacancies")
    suspend fun getVacancies(
        @QueryMap options: Map<String, String>
    ): VacanciesResponse

    @GET("vacancies/items")
    suspend fun getVacancyDetails(
        @Query("id") id: String
    ): VacancyDetailDto

    @GET("industries")
    suspend fun getIndustries(): List<ElementDto>
}
