package ru.practicum.android.diploma.domain.models



data class Vacancy(
    val id: String,
    val name: String,
    val description: String,
    val salaryFrom: Int,
    val salaryTo: Int,
    val salaryCurrency: String,
    val addressCity: String,
    val addressStreet: String,
    val addressBuilding: String,
    val experience: String,
    val schedule: String,
    val employment: String,
    val contactsName: String,
    val contactsEmail: String,
    val contactsPhone: List<String>,
    val employerName: String,
    val employerLogo: String,
    val area: FilterArea,
    val skills: List<String>,
    val url: String,
    val industry: FilterIndustry,
)
