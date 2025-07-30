package ru.practicum.android.diploma.presentation.favourites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.favourite.FavouritesInteractor
import ru.practicum.android.diploma.presentation.favourites.state.FavouritesState

class FavouritesViewModel(
    private val favouritesInteractor: FavouritesInteractor
) : ViewModel() {

    private val stateLiveData = MutableLiveData<FavouritesState>()

    fun observeState(): LiveData<FavouritesState> = stateLiveData

    fun getFavourites() {
        viewModelScope.launch {
            favouritesInteractor.getAllFavourites().collect { vacancies ->
                    stateLiveData.value = FavouritesState.Favorites(vacancies)
                }
        }
    }

}
