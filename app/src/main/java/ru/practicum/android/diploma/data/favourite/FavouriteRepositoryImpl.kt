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

    override fun getJobById(id: String): Flow<String?> {
        TODO("Not yet implemented getJobById")
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

    private fun map(favoruteJob: FavouriteJob): VacancyDetail {
        return VacancyDetail(
            id = favoruteJob.id,
            name = favoruteJob.name.orEmpty(),
            employerName = favoruteJob.employerName.orEmpty(),
            salaryFrom = favoruteJob.salaryFrom ?: 0,
            salaryTo = favoruteJob.salaryTo ?: 0,
            salaryCurrency = favoruteJob.salaryCurrency.orEmpty(),
            area = FilterArea(
                id = 0,
                name = favoruteJob.area.orEmpty(),
                areas = listOf(),
                parentId = 0
            ),
            employment = favoruteJob.employment.orEmpty(),
            schedule = favoruteJob.schedule.orEmpty(),
            experience = favoruteJob.experience.orEmpty(),
            skills = favoruteJob.keySkills?.split(", ").orEmpty(),
            description = favoruteJob.description.orEmpty(),
            url = favoruteJob.url.orEmpty(),
            contactsEmail = "",
            contactsPhone = listOf(),
            contactsName = "",
            addressStreet = "",
            employerLogo = "",
            industry = FilterIndustry(
                id = 0,
                name = ""
            ),
            addressCity = "",
            addressBuilding = "",
        )
    }
}
