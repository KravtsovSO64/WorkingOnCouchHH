package ru.practicum.android.diploma.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterSalary(
    val salary: Int?,
    val onlyWithSalary: Boolean?
) : Parcelable
