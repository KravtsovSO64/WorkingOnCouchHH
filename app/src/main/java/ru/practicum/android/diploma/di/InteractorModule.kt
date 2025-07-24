package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.impl.VacanciesInteractorImpl

val interactor = module {

    factory<VacanciesInteractor> {
        VacanciesInteractorImpl(
            vacanciesRepository = get()
        )
    }

}
