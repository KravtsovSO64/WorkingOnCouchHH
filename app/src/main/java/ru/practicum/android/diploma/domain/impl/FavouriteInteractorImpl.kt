package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.api.favourite.FavouriteRepository
import ru.practicum.android.diploma.domain.api.favourite.FavouritesInteractor
import ru.practicum.android.diploma.domain.models.VacancyDetail

class FavouriteInteractorImpl(
    private val favouriteRepository: FavouriteRepository
) : FavouritesInteractor {

    override suspend fun addToFavourites(vacancy: VacancyDetail) {
        favouriteRepository.addToFavourites(vacancy)
    }

    override suspend fun removeFromFavourites(id: String) {
        favouriteRepository.removeFromFavourites(id)
    }

    override suspend fun updateFavouriteJob() {
        TODO("Not yet implemented")
    }

    override fun getJobById(id: String): Flow<String?> {
        TODO("Not yet implemented")
    }

    override suspend fun getAllFavourites(): Flow<List<VacancyDetail>> {
        return favouriteRepository.getFavourites()
    }

    override fun checkJobInFavourites(id: String): Flow<Boolean> {
        return favouriteRepository.checkJobInFavourites(id)
    }
}
