package ru.practicum.android.diploma.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.filter.FilterInteractor
import ru.practicum.android.diploma.domain.models.ErrorCode
import ru.practicum.android.diploma.domain.models.ErrorType
import ru.practicum.android.diploma.domain.models.ResourceVacancy
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.search.state.SearchState
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(
    private val vacanciesInteractor: VacanciesInteractor,
    private val filterInteractor: FilterInteractor,
) : ViewModel() {
    private val searchState = MutableLiveData<SearchState>(SearchState.Start)
    private val totalFoundLiveData = MutableLiveData<String>()

    private val trackSearchDebounce =
        debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) { changedText ->
            search(changedText)
        }

    private val hasFilters = MutableLiveData<Boolean>()

    fun observeHasFilters(): LiveData<Boolean> = hasFilters
    fun observeSearchState(): LiveData<SearchState> = searchState
    fun observeTotalFoundLiveData(): LiveData<String> = totalFoundLiveData

    private var latestSearchText: String? = null
    private var currentPage: Int = 0
    private var maxPages: Int? = null
    private var isNextPageLoading: Boolean = false
    private var searching: Boolean = false
    private val vacancyList = mutableListOf<Vacancy>()

    fun checkFilters() {
        val currentFilters = filterInteractor.getFilter()
        if (
            currentFilters?.area == null &&
            currentFilters?.salary == null &&
            currentFilters?.industry == null
        ) {
            hasFilters.postValue(false)
        } else {
            hasFilters.postValue(true)
        }
    }

    fun onDebounceSearchTextChanged(
        p0: CharSequence?,
    ) {
        if (p0.toString().isNotBlank() && latestSearchText != p0.toString() && !searching) {
            latestSearchText = p0.toString()
            trackSearchDebounce(p0.toString())
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
            val filterParams = filterInteractor.getFilter()
            searchState.postValue(SearchState.Loading)
            vacanciesInteractor.searchVacancies(
                text = text,
                page = 0,
                area = filterParams?.area?.region?.id,
                industry = filterParams?.industry?.id,
                salary = filterParams?.salary?.salary,
                onlyWithSalary = filterParams?.salary?.onlyWithSalary ?: false
            )
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
                                searchState.postValue(SearchState.Content(vacancyList, false, hasError = false))
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
                            searchState.postValue(SearchState.Content(vacancyList, true, hasError = true))
                        }

                        is ResourceVacancy.Success -> {
                            currentPage += 1
                            vacancyList.addAll(resource.data.items)
                            searchState.postValue(SearchState.Content(vacancyList, true, hasError = false))
                        }
                    }

                }
                isNextPageLoading = false
            }
        }
    }

    fun onClearText() {
        trackSearchDebounce(SEARCH_FIELD_DEF)
        clear()
    }

    private fun formatVacancies(count: Int): String {
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
        searchState.value = SearchState.Start
    }

    companion object {
        private const val SEARCH_FIELD_DEF = ""
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
