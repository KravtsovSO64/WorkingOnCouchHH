package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.data.favourite.FavouriteRepositoryImpl
import ru.practicum.android.diploma.domain.favourite.FavouriteRepository
import ru.practicum.android.diploma.domain.search.SearchRepository
import ru.practicum.android.diploma.data.search.SearchRepositoryIml

val repository = module {
    single<FavouriteRepository> {
        FavouriteRepositoryImpl(
            get()
        )
    }

    single<SearchRepository> {
        SearchRepositoryIml()
    }
}
