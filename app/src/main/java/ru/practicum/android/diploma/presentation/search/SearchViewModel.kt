package ru.practicum.android.diploma.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.models.ErrorCode
import ru.practicum.android.diploma.domain.models.ErrorType
import ru.practicum.android.diploma.domain.models.ResourceVacancy
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.search.state.SearchState

class SearchViewModel(private val vacanciesInteractor: VacanciesInteractor) : ViewModel() {
    private val searchState = MutableLiveData<SearchState>(SearchState.Start)
    private val searchTextState = MutableLiveData("")
    private val totalFoundLiveData = MutableLiveData<String>()

    fun observeSearchTextState(): LiveData<String> = searchTextState
    fun observeSearchState(): LiveData<SearchState> = searchState
    fun observeTotalFoundLiveData(): LiveData<String> = totalFoundLiveData

    private var latestSearchText: String? = null
    private var currentPage: Int = 0
    private var maxPages: Int? = null
    private var isNextPageLoading: Boolean = false
    private var searching: Boolean = false
    private val vacancyList = mutableListOf<Vacancy>()

    fun onSearchTextChanged(
        p0: CharSequence?,
    ) {
        if (p0.toString().isNotBlank() && latestSearchText != p0.toString() && !searching) {
            latestSearchText = p0.toString()
        }

    }

    fun onEditorActionDone() {
        if (searchTextState.value.toString() != latestSearchText) {
            searchTextState.value = latestSearchText
            search(searchTextState.value.toString())
        }
    }

    private fun search(text: String) {
        if (text.isBlank() || searching) {
            return
        }
        searching = true
        currentPage = 0
        maxPages = null
        vacancyList.clear()
        viewModelScope.launch {
            searchState.postValue(SearchState.Loading)
            vacanciesInteractor.searchVacancies(
                // TODO
                text = text,
                page = 0,
                area = null,
                industry = null,
                salary = null,
                onlyWithSalary = false)
                .catch {
                    searchState.postValue(SearchState.Error(ErrorType.SERVER_ERROR))
                }
                .collect { resource ->
                    when (resource) {
                        is ResourceVacancy.Error -> {
                            handleErrorCode(resource.code)
                        }

                        is ResourceVacancy.Success -> {
                            if (resource.data.items.isEmpty()) {
                                totalFoundLiveData.value = formatVacancies(0)
                                searchState.postValue(SearchState.Error(ErrorType.EMPTY))
                            } else {
                                currentPage = 0
                                maxPages = resource.data.pages
                                totalFoundLiveData.value = resource.data.found.toString()
                                vacancyList.addAll(resource.data.items)
                                searchState.postValue(SearchState.Content(vacancyList, false))
                                totalFoundLiveData.value = formatVacancies(resource.data.found)
                            }
                        }
                    }
                    searching = false
                }
        }
    }

    private fun handleErrorCode(code: Int) {
        when (code) {
            ErrorCode.NO_CONNECTION -> {
                searchState.postValue(SearchState.Error(ErrorType.NO_CONNECTION))
            }

            else -> searchState.postValue(SearchState.Error(ErrorType.SERVER_ERROR))
        }
    }

    fun loadNextPage() {
        if (currentPage != maxPages && maxPages != null && !isNextPageLoading) {
            isNextPageLoading = true
            viewModelScope.launch {
                searchState.postValue(SearchState.PageLoading)
                vacanciesInteractor.searchVacancies(
                    text = latestSearchText!!,
                    page = currentPage,
                    area = null,
                    industry = null,
                    salary = null,
                    onlyWithSalary = false
                ).catch {
                    currentPage = 0
                    maxPages = null
                    searchState.postValue(SearchState.Error(ErrorType.SERVER_ERROR))
                }.collect { resource ->
                    when (resource) {
                        is ResourceVacancy.Error -> {
                            searchState.postValue(SearchState.Content(vacancyList, true))
                        }

                        is ResourceVacancy.Success -> {
                            currentPage += 1
                            vacancyList.addAll(resource.data.items)
                            searchState.postValue(SearchState.Content(vacancyList, true))
                        }
                    }

                }
                isNextPageLoading = false
            }
        }
    }

    fun onClearText() {
        clear()
    }

    fun formatVacancies(count: Int): String {
        return when {
            count == DECLENSION_0 -> "Таких вакансий нет"
            count % PERCENT_100 in DECLENSION_11..DECLENSION_14 -> "Найдено $count вакансий"
            count % PERCENT_10 == DECLENSION_1 -> "Найдена $count вакансия"
            count % PERCENT_10 in DECLENSION_2..DECLENSION_4 -> "Найдено $count вакансии"
            else -> "Найдено $count вакансий"
        }
    }

    private fun clear() {
        latestSearchText = ""
        searchTextState.value = ""
        searchState.value = SearchState.Start
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val PERCENT_100 = 100
        private const val PERCENT_10 = 10
        private const val DECLENSION_11 = 11
        private const val DECLENSION_14 = 14
        private const val DECLENSION_0 = 0
        private const val DECLENSION_1 = 1
        private const val DECLENSION_2 = 2
        private const val DECLENSION_4 = 4
    }
}
