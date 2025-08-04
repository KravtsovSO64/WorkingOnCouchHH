package ru.practicum.android.diploma.di

import org.koin.dsl.module
import ru.practicum.android.diploma.ui.team.SwipeAdapter

val uiModule = module {
    factory {
        SwipeAdapter()
    }

}
