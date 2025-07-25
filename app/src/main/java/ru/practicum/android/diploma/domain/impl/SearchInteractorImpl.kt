package ru.practicum.android.diploma.domain.impl

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import ru.practicum.android.diploma.domain.models.ResourceVacancy
import ru.practicum.android.diploma.domain.search.SearchInteractor
import ru.practicum.android.diploma.domain.search.SearchRepository

class SearchInteractorImpl(
    private val searchRepository: SearchRepository,
) : SearchInteractor {

    override suspend fun search(text: String, page: Int): Flow<ResourceVacancy> = flow {
        val resource = searchRepository.search( text, page, PAGE_SIZE)
        emit(resource)
    }.flowOn(Dispatchers.IO)

    companion object {
        const val PAGE_SIZE = 20
    }
}
