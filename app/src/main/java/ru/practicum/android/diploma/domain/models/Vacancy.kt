package ru.practicum.android.diploma.domain.models

data class Vacancy(
    val id: String,
    val name: String,
    val salaryFrom: Int,
    val salaryTo: Int,
    val salaryCurrency: String,
    val addressCity: String,
    val employerName: String,
    val employerLogo: String,
    val area: FilterArea,
    val industry: FilterIndustry,
)
