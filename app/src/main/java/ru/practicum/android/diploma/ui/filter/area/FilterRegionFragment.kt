package ru.practicum.android.diploma.ui.filter.area

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterRegionBinding
import ru.practicum.android.diploma.domain.models.FilterArea
import ru.practicum.android.diploma.presentation.filter.area.FilterRegionViewModel
import ru.practicum.android.diploma.presentation.filter.area.state.FilterRegionState

class FilterRegionFragment: Fragment() {
    private val viewModel: FilterRegionViewModel by viewModel()

    private var _binding: FragmentFilterRegionBinding? = null
    private val binding get() = _binding!!

    private val countryId by lazy { arguments?.getInt("countryId", -1) ?: -1 }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFilterRegionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBottomNavigation(false)
        clickHandler()

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

    override fun onResume() {
        super.onResume()
        showBottomNavigation(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        showBottomNavigation(true)
    }

    private fun clickHandler() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
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
}
