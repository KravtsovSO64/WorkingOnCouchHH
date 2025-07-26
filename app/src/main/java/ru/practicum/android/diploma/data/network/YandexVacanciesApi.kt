package ru.practicum.android.diploma.data.network

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query
import ru.practicum.android.diploma.data.dto.VacancyResponse

interface YandexVacanciesApi {
    //@Headers("Authorization: Bearer")
    @GET("vacancies")
    suspend fun getVacancies(
        //@Header("Authorization") token: String,
        @Query("text") text: String,
        @Query("page") page: Int // Номер страницы списка вакансий
    ): VacancyResponse
}
