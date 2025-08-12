package ru.practicum.android.diploma.ui.filter.area

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFilterPlaceWorkBinding
import ru.practicum.android.diploma.domain.models.FilterArea
import ru.practicum.android.diploma.presentation.filter.area.FilterAreaViewModel

class FilterPlaceWorkFragment : Fragment() {
    private val viewModel: FilterAreaViewModel by viewModel()
    private var _binding: FragmentFilterPlaceWorkBinding? = null
    private val binding get() = _binding!!
    private var countryId: Int = -1
    private var region: FilterArea? = null
    private var area: List<FilterArea>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentFragmentManager.setFragmentResultListener(
            FILTER_AREA,
            this
        ) { _, bundle ->
            region = bundle.getParcelable<FilterArea>(REGION)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilterPlaceWorkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        showBottomNavigation(false)
        setObserverEditeCountry()
        setObserverEditeRegion()
        clickHandler()
        startObserving()
    }

    private fun startObserving() {
        with(viewModel) {
            listOfRegions.observe(viewLifecycleOwner) { list ->
                area = list
                setupCountryDropdown(list)
            }

            stateLoad.observe(viewLifecycleOwner) { state ->
                if (state) {
                    with(binding) {
                        content.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                    }
                } else {
                    with(binding) {
                        content.visibility = View.VISIBLE
                        progressBar.visibility = View.GONE
                    }
                }
            }

            filter.observe(viewLifecycleOwner) { filter ->
                with(binding) {
                    countryAutoComplete.setText(filter?.area?.country?.name ?: "")
                    regionAutoComplete.setText(filter?.area?.region?.name ?: "")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigation(false)

        if (region != null) {
            viewModel.reWriteFilterRegion(region!!)
            if (binding.countryAutoComplete.text.isBlank()) {
                val foundItem = area?.firstOrNull { it.id == region?.parentId }
                viewModel.reWriteFilter(foundItem!!, region!!)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        showBottomNavigation(true)
    }

    private fun clickHandler() {
        with(binding) {
            regionAutoComplete.setOnClickListener {
                val bundle = bundleOf(COUNTRY_ID to countryId)
                findNavController().navigate(
                    R.id.action_filterPlaceWorkFragment_to_filterRegionFragment,
                    bundle)
            }

            toolbar.setOnClickListener {
                findNavController().popBackStack()
            }

            btnChoose.setOnClickListener {
               viewModel.saveChange()
                findNavController().popBackStack()
            }
        }
    }

    private fun setObserverEditeCountry() {
        val countryInputLayout = binding.countryInputLayout
        val autoCompleteTextView = binding.countryAutoComplete

        countryInputLayout.endIconDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.ic_arrow_forward)

        autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val hasText = !s.isNullOrEmpty()
                countryInputLayout.endIconDrawable = ContextCompat.getDrawable(
                    requireContext(),
                    if (hasText) R.drawable.ic_close else R.drawable.ic_arrow_forward
                )
                binding.btnChoose.visibility = if (hasText || binding.regionAutoComplete.text.isNotBlank()) View.VISIBLE else View.GONE
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        countryInputLayout.setEndIconOnClickListener {
            if (autoCompleteTextView.text.isNotBlank()) {
                autoCompleteTextView.text?.clear()
            }
        }
    }

    private fun setObserverEditeRegion() {
        val regionInputLayout = binding.regioInputLayout
        val autoCompleteTextView = binding.regionAutoComplete

        autoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val hasText = !s.isNullOrEmpty()
                regionInputLayout.endIconDrawable = ContextCompat.getDrawable(
                    requireContext(),
                    if (hasText) R.drawable.ic_close else R.drawable.ic_arrow_forward
                )
                binding.btnChoose.visibility = if (hasText || binding.countryAutoComplete.text.isNotBlank()) View.VISIBLE else View.GONE
            }
        })

        regionInputLayout.setEndIconOnClickListener {
            if (autoCompleteTextView.text.isNotBlank()) {
                autoCompleteTextView.text?.clear()
            }
        }
    }

    private fun setupCountryDropdown(list: List<FilterArea>) {
        val countries = list
            .filter { it.name.isNotBlank() }
            .map { it.name }
        val adapter = CountryAdapter(requireContext(), countries)

        with(binding.countryAutoComplete) {
            setAdapter(adapter)

            setOnItemClickListener { _, _, position, _ ->
                binding.countryInputLayout.hint = HINT
                countryId = list[position].id
                viewModel.reWriteFilterCountry(list[position])
            }

            setOnClickListener {
                if (adapter.count > 0) {
                    showDropDown()
                }
            }
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

    companion object {
        const val HINT = "Страна"
        const val FILTER_AREA = "filterArea"
        const val REGION = "region"
        const val COUNTRY_ID = "countryId"
    }
}
