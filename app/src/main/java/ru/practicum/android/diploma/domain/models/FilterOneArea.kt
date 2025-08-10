package ru.practicum.android.diploma.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterOneArea(
    val country: AreaEntity?,
    val region: AreaEntity?
) : Parcelable
