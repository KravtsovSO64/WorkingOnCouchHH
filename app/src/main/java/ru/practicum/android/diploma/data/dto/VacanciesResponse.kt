package ru.practicum.android.diploma.data.dto

import ru.practicum.android.diploma.data.dto.vacancy.VacancyDto

class VacanciesResponse(
    val found: Int,
    val pages: Int,
    val page: Int,
    val items: Array<VacancyDto>
) : Response()
