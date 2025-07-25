package ru.practicum.android.diploma.data.favourite

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.db.FavouriteJob
import ru.practicum.android.diploma.domain.favourite.FavouriteRepository

class FavouriteRepositoryImpl(private val appDatabase: AppDatabase) : FavouriteRepository {
    override suspend fun addToFavourites() {
        TODO("Not yet implemented addToFavourites")
    }

    override suspend fun removeFromFavourites(id: String) {
        TODO("Not yet implemented removeFromFavourites")
    }

    override suspend fun updateFavouriteJob() {
        TODO("Not yet implemented updateFavouriteJob")
    }

    override fun getJobById(id: String): Flow<String?> {
        TODO("Not yet implemented getJobById")
    }

    override fun getFavourites(): Flow<List<FavouriteJob>> {
        TODO("Not yet implemented getFavourites")
    }

    override fun checkJobInFavourites(id: String): Flow<Boolean> {
        TODO("Not yet implemented checkJobInFavourites")
    }
}
