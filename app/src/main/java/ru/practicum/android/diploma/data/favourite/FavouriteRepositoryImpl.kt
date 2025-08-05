package ru.practicum.android.diploma.data.favourite

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.db.FavouriteJob
import ru.practicum.android.diploma.domain.api.favourite.FavouriteRepository
import ru.practicum.android.diploma.domain.models.FilterArea
import ru.practicum.android.diploma.domain.models.FilterIndustry
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

    override suspend fun getJobById(id: String): VacancyDetail? {
        return appDatabase.favouriteDao()
            .getFavoriteVacancyById(id)
            ?.let { favoriteVacancy ->
                map(favoriteVacancy)
            }
    }

    override suspend fun getFavourites(): Flow<List<VacancyDetail>> = flow {
        emit(appDatabase.favouriteDao().getAll().map {
            map(it)
        })
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

    private fun map(favouriteJob: FavouriteJob): VacancyDetail {
        return VacancyDetail(
            id = favouriteJob.id,
            name = favouriteJob.name.orEmpty(),
            employerName = favouriteJob.employerName.orEmpty(),
            salaryFrom = favouriteJob.salaryFrom ?: 0,
            salaryTo = favouriteJob.salaryTo ?: 0,
            salaryCurrency = favouriteJob.salaryCurrency.orEmpty(),
            area = FilterArea(
                id = 0,
                name = favouriteJob.area.orEmpty(),
                areas = listOf(),
                parentId = 0
            ),
            employment = favouriteJob.employment.orEmpty(),
            schedule = favouriteJob.schedule.orEmpty(),
            experience = favouriteJob.experience.orEmpty(),
            skills = favouriteJob.keySkills?.split(", ").orEmpty(),
            description = favouriteJob.description.orEmpty(),
            url = favouriteJob.url.orEmpty(),
            contactsEmail = "",
            contactsPhone = listOf(),
            contactsName = "",
            addressStreet = "",
            employerLogo = "",
            industry = FilterIndustry(
                id = "",
                name = ""
            ),
            addressCity = "",
            addressBuilding = "",
        )
    }
}
