package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.data.dto.vacancy.VacancyDto

class VacancyResponse(
    val found: Int,
    val pages: Int,
    val page: Int,
    val items: Array<VacancyDto>
) : Response()
