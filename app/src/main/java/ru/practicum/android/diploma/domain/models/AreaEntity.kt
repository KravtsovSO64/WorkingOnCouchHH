package ru.practicum.android.diploma.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AreaEntity(
    val id: String,
    val name: String,
    val parentCountry: AreaEntity? = null
): Parcelable
