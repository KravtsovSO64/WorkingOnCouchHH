package ru.practicum.android.diploma.domain.api.favourite

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.domain.models.VacancyDetail

interface FavouriteRepository {
    suspend fun addToFavourites(vacancy: VacancyDetail)
    suspend fun removeFromFavourites(id: String)
    suspend fun updateFavouriteJob()
    suspend fun getJobById(id: String): VacancyDetail?
    suspend fun getFavourites(): Flow<List<VacancyDetail>>
    fun checkJobInFavourites(id: String): Flow<Boolean>
}
