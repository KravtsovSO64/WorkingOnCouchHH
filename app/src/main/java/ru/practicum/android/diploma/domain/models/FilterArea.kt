package ru.practicum.android.diploma.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterArea(
    val id: Int,
    val parentId: Int,
    val name: String,
    val areas: List<String>,
) : Parcelable
