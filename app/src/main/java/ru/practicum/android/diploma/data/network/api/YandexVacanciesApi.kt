package ru.practicum.android.diploma.data.network.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.practicum.android.diploma.data.dto.VacanciesResponse
import ru.practicum.android.diploma.data.dto.vacancy.VacancyDetailDto

interface YandexVacanciesApi {
    @GET("vacancies")
    suspend fun getVacancies(
        @Query("text") text: String,
        @Query("page") page: Int // Номер страницы списка вакансий
    ): VacanciesResponse

    @GET("vacancies/items")
    suspend fun getVacancyDetails(
        @Query("id") id : String
    ): VacancyDetailDto
}
