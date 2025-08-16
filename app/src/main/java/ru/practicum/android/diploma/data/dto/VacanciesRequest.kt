package ru.practicum.android.diploma.data.dto

data class VacanciesRequest(
    val text: String,
    val page: Int,
    val area: String?,
    val industry: String?,
    val salary: Int?,
    val onlyWithSalary: Boolean,
)
