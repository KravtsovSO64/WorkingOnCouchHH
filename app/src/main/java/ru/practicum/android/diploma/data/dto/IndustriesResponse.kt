package ru.practicum.android.diploma.data.dto

import ru.practicum.android.diploma.data.dto.vacancy.elements.ElementDto

class IndustriesResponse : Response() {
    var industries: List<ElementDto> = emptyList()
}
