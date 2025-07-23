package ru.practicum.android.diploma.data.dto

import com.google.gson.annotations.SerializedName
import ru.practicum.android.diploma.data.dto.elements_vacancy_dto.ElementDto
import ru.practicum.android.diploma.data.dto.elements_vacancy_dto.EmployerDto
import ru.practicum.android.diploma.data.dto.elements_vacancy_dto.SalaryDto
import ru.practicum.android.diploma.data.dto.elements_vacancy_dto.SnippetDto

data class VacancyDto(
    val name: String,
    val salary: SalaryDto?,
    val area: ElementDto?,
    val employer: EmployerDto?,
    val experience: ElementDto?,
    val snippet: SnippetDto, // !requirement!, !responsibility!
    val schedule: ElementDto, // id, !name!
    val employment: ElementDto,
    @SerializedName("professional_roles")
    val professionalRoles: List<ElementDto>?,
)
