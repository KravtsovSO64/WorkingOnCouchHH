package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.data.dto.vacancy.VacancyDto

class VacanciesResponse(
    @SerializedName("items") val vacanciesList: List<VacancyDto>
) : Response()
