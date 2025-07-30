package ru.practicum.android.diploma.presentation.vacancy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.api.favourite.FavouritesInteractor
import ru.practicum.android.diploma.domain.models.ErrorCode
import ru.practicum.android.diploma.domain.models.ErrorType
import ru.practicum.android.diploma.domain.models.ResourceVacancyDetail
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.presentation.vacancy.state.VacancyScreenState

/*
  В рамках данной VM можно:
  - добавить вакансию в избранное;
  - удалить вакансию из списка избранное;
  - уточнить, является ли данная вакансия избранной;
  - получить вакансию по id.
  */

class VacancyViewModel(
    private val favouritesInteractor: FavouritesInteractor,
    private val vacancyInteractor: VacanciesInteractor
) : ViewModel() {

    private val _stateScreen = MutableLiveData<VacancyScreenState>()
    val stateScreen: LiveData<VacancyScreenState> get() = _stateScreen

    private val _stateFavourite = MutableLiveData<Boolean>()
    val stateFavourite: LiveData<Boolean> get() = _stateFavourite

    fun getJobDetails(id: String) {
        _stateScreen.postValue(VacancyScreenState.Loading)

        viewModelScope.launch {
            vacancyInteractor.detailsVacancy(id)
                .collect { content ->
                    when (content) {
                        is ResourceVacancyDetail.Error -> {
                            handleErrorCode(content.code)
                        }

                        is ResourceVacancyDetail.Success -> {
                            _stateScreen.postValue(VacancyScreenState.Content(content.data))
                        }
                    }
                }

        }
    }

    fun addToFavourite(vacancy: VacancyDetail) {
        viewModelScope.launch {
            favouritesInteractor.addToFavourites(vacancy)
            _stateFavourite.postValue(true)
        }
    }

    fun removeFromFavourites(id: String) {
        viewModelScope.launch {
            favouritesInteractor.removeFromFavourites(id)
            _stateFavourite.postValue(false)
        }
    }

    fun checkJobInFavourites(id: String) {
        viewModelScope.launch {
            favouritesInteractor.checkJobInFavourites(id)
                .collect { isFavourite ->
                    _stateFavourite.postValue(isFavourite)
                }
        }
    }

    private fun handleErrorCode(code: Int) {
        when (code) {
            ErrorCode.NO_CONNECTION -> {
                _stateScreen.postValue(VacancyScreenState.Error(ErrorType.NO_CONNECTION))
            }

            ErrorCode.NOT_FOUND -> {
                _stateScreen.postValue(VacancyScreenState.Error(ErrorType.EMPTY))
            }

            else -> {
                _stateScreen.postValue(VacancyScreenState.Error(ErrorType.SERVER_ERROR))
            }
        }
    }
}
