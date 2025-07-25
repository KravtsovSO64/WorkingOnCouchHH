package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.domain.impl.SearchInteractorImpl
import ru.practicum.android.diploma.domain.search.SearchInteractor

val interactor = module {
    factory<SearchInteractor> {
        SearchInteractorImpl(
            get(),
        )
    }
}
