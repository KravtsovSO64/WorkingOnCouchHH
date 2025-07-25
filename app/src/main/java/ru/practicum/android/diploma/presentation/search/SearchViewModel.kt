package ru.practicum.android.diploma.presentation.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.models.ErrorCode
import ru.practicum.android.diploma.domain.models.ErrorType
import ru.practicum.android.diploma.domain.models.ResourceVacancy
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.domain.search.SearchInteractor
import ru.practicum.android.diploma.presentation.search.state.SearchState
import ru.practicum.android.diploma.util.debounce

class SearchViewModel(private val searchInteractor: SearchInteractor) : ViewModel() {
    private val searchState = MutableLiveData<SearchState>(SearchState.Start)
    val searchTextState = MutableLiveData("")
    private val totalFoundLiveData = MutableLiveData<Int>()

    fun observeSearchTextState(): LiveData<String> = searchTextState
    fun observeSearchState(): LiveData<SearchState> = searchState
    fun observeTotalFoundLiveData(): LiveData<Int> = totalFoundLiveData

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
            searchInteractor.search(text = text, 0)
                .catch {
                    searchState.postValue(SearchState.Error(ErrorType.SERVER_ERROR))
                }
                .collect { resource ->
                    when (resource) {
                        is ResourceVacancy.Error -> {
                            handleErrorCode(resource.code)
                        }

                        is ResourceVacancy.Success -> {
                            if (resource.data.isEmpty()) {
                                totalFoundLiveData.value = 0
                                searchState.postValue(SearchState.Error(ErrorType.EMPTY))
                            } else {
                                currentPage = 1
                                maxPages = resource.pages
                                totalFoundLiveData.value = resource.total
                                vacancyList.addAll(resource.data)
                                searchState.postValue(SearchState.Content(vacancyList, false))
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
                searchInteractor.search(latestSearchText!!, currentPage).catch {
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
                            vacancyList.addAll(resource.data)
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

    private fun clear() {
        latestSearchText = ""
        searchTextState.value = ""
        searchState.value = SearchState.Start
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}
