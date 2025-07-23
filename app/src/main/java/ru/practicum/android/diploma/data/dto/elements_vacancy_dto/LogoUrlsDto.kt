package ru.practicum.android.diploma.data.dto.elements_vacancy_dto

import com.google.gson.annotations.SerializedName

data class LogoUrlsDto(
    val original: String,
    @SerializedName("90") val size90: String,
    @SerializedName("240") val size240: String,
)
