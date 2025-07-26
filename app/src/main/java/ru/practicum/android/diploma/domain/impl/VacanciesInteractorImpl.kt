package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.practicum.android.diploma.data.network.VacanciesRepository
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.Vacancy

class VacanciesInteractorImpl (
    private val vacanciesRepository: VacanciesRepository
) : VacanciesInteractor {

    override fun searchVacancies(text:String, page: Int): Flow<List<Vacancy>> {
        // добавить проверку состояния
        return vacanciesRepository.searchVacancies(text, page)

    }
}
