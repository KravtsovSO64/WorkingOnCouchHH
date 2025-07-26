package ru.practicum.android.diploma.data.network.api

import retrofit2.http.GET
import retrofit2.http.Query
import ru.practicum.android.diploma.data.dto.VacancyResponse

interface YandexVacanciesApi {
    @GET("vacancies")
    suspend fun getVacancies(
        @Query("text") text: String, @Query("page") page: Int // Номер страницы списка вакансий
    ): VacancyResponse
}
