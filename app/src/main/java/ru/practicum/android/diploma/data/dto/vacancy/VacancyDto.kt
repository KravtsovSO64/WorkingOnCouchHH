package ru.practicum.android.diploma.data.dto.vacancy

import ru.practicum.android.diploma.data.dto.vacancy.elements.AddressDto
import ru.practicum.android.diploma.data.dto.vacancy.elements.EmployerDto
import ru.practicum.android.diploma.data.dto.vacancy.elements.FilterAreaDto
import ru.practicum.android.diploma.data.dto.vacancy.elements.FilterIndustryDto
import ru.practicum.android.diploma.data.dto.vacancy.elements.SalaryDto

data class VacancyDto(
    val id: String,
    val name: String,
    val salary: SalaryDto?,
    val address: AddressDto?,
    val employer: EmployerDto,
    val area: FilterAreaDto,
    val industry: FilterIndustryDto
)
