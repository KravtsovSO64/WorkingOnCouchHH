package ru.practicum.android.diploma.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
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
) : Parcelable
