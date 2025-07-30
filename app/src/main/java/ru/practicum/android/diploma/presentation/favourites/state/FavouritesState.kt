package ru.practicum.android.diploma.presentation.favourites.state

import ru.practicum.android.diploma.domain.models.VacancyDetail

interface FavouritesState {

    data object Loading : FavouritesState
    data object Empty : FavouritesState
    data class Favorites(
        val vacancies: List<VacancyDetail>
    ) : FavouritesState
}
