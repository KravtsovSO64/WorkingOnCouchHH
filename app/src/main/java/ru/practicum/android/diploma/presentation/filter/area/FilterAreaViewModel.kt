package ru.practicum.android.diploma.presentation.filter.area

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.practicum.android.diploma.domain.api.vacancy.VacanciesInteractor
import ru.practicum.android.diploma.domain.filter.FilterCacheInteractor
import ru.practicum.android.diploma.domain.models.AreaEntity
import ru.practicum.android.diploma.domain.models.Filter
import ru.practicum.android.diploma.domain.models.FilterArea
import ru.practicum.android.diploma.domain.models.FilterOneArea
import ru.practicum.android.diploma.domain.models.ResourceAreas


class FilterAreaViewModel(
    private val interactor: VacanciesInteractor,
    private val filterCacheInteractor: FilterCacheInteractor,
): ViewModel() {

    // Полученный список храним тут
    private val _listOfRegions = MutableLiveData<List<FilterArea>>()
    val listOfRegions: LiveData<List<FilterArea>> get() = _listOfRegions

    // Экран простой, поэтому два состояния, загрузка и результат(положительный или отрицательный)
    private val _stateLoad = MutableLiveData(true)
    val stateLoad: MutableLiveData<Boolean> get() = _stateLoad

    // Храним фильтр для заполнения полей экрана
    private val _filter = MutableLiveData<Filter>()
    val filter: LiveData<Filter> get() = _filter

    init {
        load()
        getFilter()
    }

    private fun load() {
        viewModelScope.launch {
            interactor.getAreas()
                .collect { resource ->
                    when (resource) {
                        is ResourceAreas.Success -> {
                            val areas = resource.data
                            _listOfRegions.postValue(areas)
                            _stateLoad.postValue(false)
                        }
                        is ResourceAreas.Error -> {
                            _listOfRegions.postValue(emptyList())
                            _stateLoad.postValue(false)
                        }
                    }
                }
        }
    }

    // Перезаписываем в кеш фильтр с выбранной странной
    fun reWriteFilterCountry(country: FilterArea) {
        var filter = _filter.value!!
        filter = Filter(
            area = FilterOneArea(AreaEntity(
                country.id,
                country.name,
                country.parentId
                ),
                filter.area?.region
            ),
            industry = filter.industry,
            salary = filter.salary
        )
        _filter.postValue(filter)
    }

    // Перезаписываем в кеш фильтр с выбранным регионом
    fun reWriteFilterRegion(region: FilterArea) {
        var filter = _filter.value!!
        filter = Filter(
            area = FilterOneArea(
                filter.area?.country,
                AreaEntity(
                    region.id,
                    region.name,
                    region.parentId
                )
            ),
            industry = filter.industry,
            salary = filter.salary
        )
        _filter.postValue(filter)
    }

    fun reWriteFilter(country: FilterArea, region: FilterArea) {
        var filter = _filter.value!!
        filter = Filter(
            area = FilterOneArea(
                AreaEntity(
                    country.id,
                    country.name,
                    country.parentId
                ),
                AreaEntity(
                    region.id,
                    region.name,
                    region.parentId
                )
            ),
            industry = filter.industry,
            salary = filter.salary
        )
        _filter.postValue(filter)
    }

    // Сохраняем фильтр в кеш
    fun saveChange(){
        val filter = _filter.value
        filterCacheInteractor.writeCache(
            filter!!,
            setRegion = true,
            setSalary = false,
            setIndustry = false
        )
    }

    private fun getFilter() {
        val filter = filterCacheInteractor.getCache() ?: Filter()
        _filter.postValue(filter)
    }
}
