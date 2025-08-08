package ru.practicum.android.diploma.ui.filter.industry

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterIndustryBinding
import ru.practicum.android.diploma.domain.models.FilterIndustry
import ru.practicum.android.diploma.presentation.filter.industry.FilterIndustryViewModel
import ru.practicum.android.diploma.presentation.filter.industry.state.FilterIndustryListState
import ru.practicum.android.diploma.presentation.filter.industry.state.FilterIndustryState
import ru.practicum.android.diploma.util.AbstractBindingFragment

class FilterIndustryFragment : AbstractBindingFragment<FragmentFilterIndustryBinding>() {

    private val viewModel: FilterIndustryViewModel by viewModel()
    private var currentState: FilterIndustryListState? = null

    private val adapter by lazy {
        FilterIndustryAdapter { viewModel.onChecked(it) }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFilterIndustryBinding {
        return FragmentFilterIndustryBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(
            view,
            savedInstanceState
        )
        binding.recyclerIndustries.adapter = adapter
        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.buttonSave.setOnClickListener {
            findNavController().navigateUp()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigateUp()
        }
        viewModel.observeItems().observe(viewLifecycleOwner) {
            render(it)
            currentState = it
        }

        viewModel.observeIndustryState().observe(viewLifecycleOwner) {
            showIndustryState(it)
        }

        binding.editTextIndustry.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    //
                }

                override fun afterTextChanged(p0: Editable?) {
                    viewModel.onFilterTextChanged(p0.toString())
                }
            }
        )
    }

    private fun updateTextInputLayoutIcon(text: String) {
        if (text.isNotEmpty()) {
            setClearIcon()
        } else {
            setSearchIcon()
        }
    }

    @SuppressLint("ServiceCast")
    private fun setClearIcon() {
        binding.textInputLayout.endIconDrawable = AppCompatResources.getDrawable(
            requireActivity(),
            R.drawable.ic_close
        )
        binding.textInputLayout.setEndIconOnClickListener {
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(
                Activity().currentFocus?.windowToken,
                0
            )
            viewModel.onClearText()
        }
    }

    private fun setSearchIcon() {
        binding.textInputLayout.endIconDrawable = AppCompatResources.getDrawable(
            requireActivity(),
            R.drawable.ic_search
        )
        binding.textInputLayout.setEndIconOnClickListener {}
    }

    private fun showIndustryState(state: FilterIndustryState) {
        if (viewModel.observeItems().value == null) return
        binding.buttonSave.isVisible = state.isSaveEnable
        adapter.applyFilter(state.filterText)
        if (adapter.itemCount == 0) {
            binding.groupError.isVisible = true
            binding.imageError.setImageResource(R.drawable.empty_results_cat)
            binding.textErrorMessage.text = getString(R.string.failed_to_find_industry)
        } else {
            binding.groupError.isVisible = false
        }
        updateTextInputLayoutIcon(state.filterText)
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
        adapter.setList(industries, current)
    }

    private fun showError() {
        binding.recyclerIndustries.isVisible = false
        binding.groupError.isVisible = true
        binding.buttonSave.isVisible = false
        binding.imageError.setImageResource(R.drawable.screen_placeholder_carpet)
        binding.textErrorMessage.text = getString(R.string.empty_list_error)
    }
}
