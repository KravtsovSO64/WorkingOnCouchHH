package ru.practicum.android.diploma.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.practicum.android.diploma.presentation.favourites.FavouritesViewModel
import ru.practicum.android.diploma.presentation.filter.FilterSettingsViewModel
import ru.practicum.android.diploma.presentation.filter.area.FilterAreaViewModel
import ru.practicum.android.diploma.presentation.filter.area.FilterRegionViewModel
import ru.practicum.android.diploma.presentation.filter.industry.FilterIndustryViewModel
import ru.practicum.android.diploma.presentation.search.SearchViewModel
import ru.practicum.android.diploma.presentation.vacancy.VacancyViewModel

val viewModel = module {
    viewModel {
        SearchViewModel(get(), get())
    }
    viewModel {
        VacancyViewModel(get(), get())
    }
    viewModel {
        FavouritesViewModel(get())
    }
    viewModel {
        FilterSettingsViewModel(get(), get())
    }
    viewModel {
        FilterIndustryViewModel(get(), get())
    }
    viewModel {
        FilterAreaViewModel(get(), get())
    }
    viewModel {
        FilterRegionViewModel(get())
    }
}
