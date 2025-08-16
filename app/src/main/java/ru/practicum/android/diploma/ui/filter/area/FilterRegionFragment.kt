package ru.practicum.android.diploma.ui.filter.area

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterRegionBinding
import ru.practicum.android.diploma.domain.models.FilterArea
import ru.practicum.android.diploma.presentation.filter.area.FilterRegionViewModel
import ru.practicum.android.diploma.presentation.filter.area.state.FilterRegionState

class FilterRegionFragment : Fragment() {
    private val viewModel: FilterRegionViewModel by viewModel()
    private var _binding: FragmentFilterRegionBinding? = null
    private val binding get() = _binding!!
    private val countryId by lazy { arguments?.getInt("countryId", -1) ?: -1 }
    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterRegionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        showBottomNavigation(false)
        clickHandler()
        setupSearchInput()
        startObserving()
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigation(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        showBottomNavigation(true)
    }

    private fun startObserving() {
        viewModel.getListArea(countryId)

        viewModel.stateScreen.observe(viewLifecycleOwner) { state ->
            when (state) {
                is FilterRegionState.Content -> showContent(state.data)
                is FilterRegionState.Error -> showError()
                FilterRegionState.Loading -> showLoading()
            }
        }
    }

    private fun showLoading() {
        with(binding) {
            imageInfo.visibility = View.GONE
            textInfo.visibility = View.GONE
            recyclerViewVacancies.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }
    }

    private fun showError() {
        with(binding) {
            imageInfo.visibility = View.VISIBLE
            textInfo.visibility = View.VISIBLE
            recyclerViewVacancies.visibility = View.GONE
            progressBar.visibility = View.GONE
        }
    }

    private fun showContent(listArea: List<FilterArea>) {
        with(binding) {
            imageInfo.visibility = View.GONE
            textInfo.visibility = View.GONE
            recyclerViewVacancies.visibility = View.VISIBLE
            progressBar.visibility = View.GONE

            recyclerViewVacancies.layoutManager = LinearLayoutManager(requireContext())

            viewModel.regions.observe(viewLifecycleOwner) {
                recyclerViewVacancies.adapter = RegionAdapter(it) { selectedArea ->
                    handleAreaSelection(selectedArea)
                }
            }

            recyclerViewVacancies.adapter = RegionAdapter(listArea) { selectedArea ->
                handleAreaSelection(selectedArea)
            }
        }
    }

    private fun handleAreaSelection(region: FilterArea) {
        val bundle = bundleOf("region" to region)
        parentFragmentManager.setFragmentResult("filterArea", bundle)
        findNavController().popBackStack()
    }

    private fun clickHandler() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setupSearchInput() {
        with(binding) {
            editTextSearchInput.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    //
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    //
                }

                override fun afterTextChanged(editable: Editable?) {
                    searchJob?.cancel()
                    val query = editable?.toString()?.trim() ?: ""

                    updateSearchIcon(query.isNotBlank())

                    searchJob = lifecycleScope.launch {
                        delay(DELAY)
                        viewModel.filterRegions(query)
                    }
                }
            })

            editTextSearchInput.setOnClickListener {
                if (!binding.editTextSearchInput.text.isNullOrEmpty()) {
                    searchJob?.cancel()
                    binding.editTextSearchInput.text?.clear()
                    viewModel.filterRegions("")
                }
            }

            editTextSearchInput.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    searchJob?.cancel()
                    val query = binding.editTextSearchInput.text?.toString()?.trim() ?: ""
                    viewModel.filterRegions(query)
                    hideKeyboard()
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun hideKeyboard() {
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(binding.editTextSearchInput.windowToken, 0)
    }

    private fun updateSearchIcon(hasText: Boolean) {
        binding.textInputLayout.endIconDrawable = ContextCompat.getDrawable(
            requireContext(),
            if (hasText) R.drawable.ic_close else R.drawable.ic_search
        )
    }

    private fun showBottomNavigation(flag: Boolean) {
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val divider = requireActivity().findViewById<View>(R.id.divider)

        if (flag) {
            bottomNavigationView.visibility = View.VISIBLE
            divider.visibility = View.VISIBLE
        } else {
            bottomNavigationView.visibility = View.GONE
            divider.visibility = View.GONE
        }
    }

    companion object {
        private const val DELAY = 2000L
    }
}
