package ru.practicum.android.diploma.di

import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.data.db.AppDatabase
import ru.practicum.android.diploma.data.network.YandexVacanciesApi
import ru.practicum.android.diploma.data.network.NetworkClient
import ru.practicum.android.diploma.data.network.RetrofitNetworkClient

val data = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            "database.db"
        ).fallbackToDestructiveMigration(false).build()
    }

    single<NetworkClient> {
        RetrofitNetworkClient(yandexVacanciesApi = get())
    }

    single<YandexVacanciesApi> {
        Retrofit.Builder()
            .baseUrl("https://practicum-diploma-8bc38133faba.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(YandexVacanciesApi::class.java)
    }
}

