package ru.practicum.android.diploma.data.dto

data class VacanciesRequest(
    val text: String,
    val page: Int,
    val area: Int?,
    val industry: Int?,
    val salary: Int?,
    val onlyWithSalary: Boolean,
)
