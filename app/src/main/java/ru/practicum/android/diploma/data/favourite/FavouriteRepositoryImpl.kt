package ru.practicum.android.diploma.data.favourite

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.db.FavouriteJob
import ru.practicum.android.diploma.domain.api.favourite.FavouriteRepository
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.models.VacancyDetail

class FavouriteRepositoryImpl(
    private val appDatabase: AppDatabase
) : FavouriteRepository {

    override suspend fun addToFavourites(vacancy: VacancyDetail) {
        appDatabase.favouriteDao().insert(map(vacancy))
    }

    override suspend fun removeFromFavourites(id: String) {
        appDatabase.favouriteDao().delete(id)
    }

    override suspend fun updateFavouriteJob() {
        TODO("Not yet implemented updateFavouriteJob")
    }

    override fun getJobById(id: String): Flow<String?> {
        TODO("Not yet implemented getJobById")
    }

    override fun getFavourites(): Flow<List<Vacancy>> {
        TODO("Not yet implemented getFavourites")
    }

    override fun checkJobInFavourites(id: String): Flow<Boolean> {
       return appDatabase.favouriteDao().isFavorite(id)
    }

    private fun map(vacancy: VacancyDetail): FavouriteJob {
        return FavouriteJob(
            id = vacancy.id,
            name = vacancy.name,
            employerName = vacancy.employerName,
            salaryFrom = vacancy.salaryFrom,
            salaryTo = vacancy.salaryTo,
            salaryCurrency = vacancy.salaryCurrency,
            area = vacancy.area.name,
            employment = vacancy.employment,
            schedule = vacancy.schedule,
            experience = vacancy.experience,
            keySkills = vacancy.skills.joinToString(", "),
            description = vacancy.description,
            url = vacancy.url
        )
    }
}
