package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.favourite.FavouriteRepositoryImpl
import ru.practicum.android.diploma.data.network.interfaces.VacanciesRepository
import ru.practicum.android.diploma.data.network.impl.VacanciesRepositoryImpl
import ru.practicum.android.diploma.domain.api.favourite.FavouriteRepository

val repository = module {
    single<FavouriteRepository> {
        FavouriteRepositoryImpl(
            appDatabase = get()
        )
    }

    factory<VacanciesRepository> {
        VacanciesRepositoryImpl(
            networkClient = get()
        )
    }
}
