package ru.practicum.android.diploma.data.dto

import ru.practicum.android.diploma.data.dto.vacancy.elements.FilterAreaDto

class AreasResponse : Response() {
    var areas: List<FilterAreaDto> = emptyList()
}
