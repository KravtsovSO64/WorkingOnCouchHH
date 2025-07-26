package ru.practicum.android.diploma.data.dto.vacancy

import ru.practicum.android.diploma.data.dto.vacancy.elements.AddressDto
import ru.practicum.android.diploma.data.dto.vacancy.elements.ContactsDto
import ru.practicum.android.diploma.data.dto.vacancy.elements.ElementDto
import ru.practicum.android.diploma.data.dto.vacancy.elements.EmployerDto
import ru.practicum.android.diploma.data.dto.vacancy.elements.SalaryDto

data class VacancyDto(
    val id: String,
    val name: String,
    val description: String,
    val salary: SalaryDto?,
    val address: AddressDto?,
    val experience: ElementDto,
    val schedule: ElementDto,
    val employment: ElementDto,
    val contacts: ContactsDto?,
    val employer: EmployerDto,
    //val area: FilterArea //id, parentId, name, areas
    val skills: List<String>?,
    val url: String,
    //val industry: FilterIndustry //id, name


)
