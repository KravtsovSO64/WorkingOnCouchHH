package ru.practicum.android.diploma.data.dto.vacancy

import ru.practicum.android.diploma.data.dto.Response
import ru.practicum.android.diploma.data.dto.vacancy.elements.AddressDto
import ru.practicum.android.diploma.data.dto.vacancy.elements.ContactsDto
import ru.practicum.android.diploma.data.dto.vacancy.elements.ElementDto
import ru.practicum.android.diploma.data.dto.vacancy.elements.EmployerDto
import ru.practicum.android.diploma.data.dto.vacancy.elements.FilterAreaDto
import ru.practicum.android.diploma.data.dto.vacancy.elements.FilterIndustryDto
import ru.practicum.android.diploma.data.dto.vacancy.elements.SalaryDto

data class VacancyDetailDto(
    val id: String,
    val name: String,
    val salary: SalaryDto?,
    val address: AddressDto?,
    val experience: ElementDto,
    val schedule: ElementDto,
    val employment: ElementDto,
    val contacts: ContactsDto?,
    val description: String,
    val employer: EmployerDto,
    val area: FilterAreaDto,
    val skills: List<String>?,
    val url: String,
    val industry: FilterIndustryDto,
) : Response()
