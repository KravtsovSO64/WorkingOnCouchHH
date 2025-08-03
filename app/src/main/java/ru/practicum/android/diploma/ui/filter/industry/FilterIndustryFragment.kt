package ru.practicum.android.diploma.ui.filter.industry

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterIndustryBinding
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.presentation.filter.industry.FilterIndustryViewModel
import ru.practicum.android.diploma.presentation.filter.industry.state.FilterIndustryListState
import ru.practicum.android.diploma.util.AbstractBindingFragment

class FilterIndustryFragment: AbstractBindingFragment<FragmentFilterIndustryBinding>() {

    private val viewModel: FilterIndustryViewModel by viewModel()

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFilterIndustryBinding {
        return FragmentFilterIndustryBinding.inflate(inflater, container, false)
    }

    private fun render(state: FilterIndustryListState) {
        when (state) {
            is FilterIndustryListState.Content -> showContent(state.industries, state.current)
            is FilterIndustryListState.Error -> showError()
        }
    }

    private fun showContent(industries: List<FilterIndustry>, current: FilterIndustry?) {
        binding.groupError.isVisible = false
        binding.recyclerIndustries.isVisible = true
        binding.buttonSave.isVisible = current != null
        //adapter.setList(industries, current)
    }

    private fun showError() {
        binding.recyclerIndustries.isVisible = false
        binding.groupError.isVisible = true
        binding.buttonSave.isVisible = false
        binding.imageError.setImageResource(R.drawable.screen_placeholder_carpet)
        binding.textErrorMessage.text = getString(R.string.empty_list_error)
    }
}
