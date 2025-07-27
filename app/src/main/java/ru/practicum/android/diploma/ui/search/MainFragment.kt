package ru.practicum.android.diploma.ui.search

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
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentMainBinding
import ru.practicum.android.diploma.domain.models.ErrorType
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.search.SearchViewModel
import ru.practicum.android.diploma.presentation.search.state.SearchState
import ru.practicum.android.diploma.util.AbstractBindingFragment

class MainFragment : AbstractBindingFragment<FragmentMainBinding>() {

    private val viewModel: SearchViewModel by viewModel()
    private val adapter by lazy {
        JobAdapter { id: String -> openVacancy(id) }
    }

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)


        setSearchIcon()
        setUpListeners()

        viewModel.observeSearchTextState().observe(viewLifecycleOwner) {
            updateTextInputLayoutIcon(it)
        }


        viewModel.observeSearchState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchState.Start -> showStart()
                is SearchState.Content -> {
                    showContent(state.data, state.paging)
                }

                is SearchState.Loading -> showLoading()
                is SearchState.PageLoading -> {
                    showPageLoading()
                }

                is SearchState.Error -> {
                    updateResultText(0)
                    showError(state.type)
                }
            }
        }

        viewModel.observeTotalFoundLiveData().observe(viewLifecycleOwner) {
            updateResultText(it)
        }
    }

    private fun setUpListeners() {
        binding.editTextSearchInput.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                viewModel.onSearchTextChanged(p0.toString())
            }
        }
        )
        binding.editTextSearchInput.setOnEditorActionListener { _, actionId, _ ->
            viewModel.onEditorActionDone()
            false
        }

        binding.recyclerViewVacancies.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewVacancies.adapter = adapter
        binding.recyclerViewVacancies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    val pos =
                        (binding.recyclerViewVacancies.layoutManager as LinearLayoutManager)
                            .findLastVisibleItemPosition()
                    val itemsCount = adapter.itemCount
                    if (pos >= itemsCount - 1) {
                        viewModel.loadNextPage()
                    }
                }
            }
        })

    }


    private fun updateTextInputLayoutIcon(text: String) {
        if (text.isNotEmpty()) {
            setClearIcon()
        } else {
            binding.editTextSearchInput.text?.clear()
            setSearchIcon()
        }
    }

    @SuppressLint("ServiceCast")
    private fun setClearIcon() {
        binding.imageEndIconDrawable.setImageResource(R.drawable.ic_close)
        binding.imageEndIconDrawable.setOnClickListener {
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
        binding.imageEndIconDrawable.setImageResource(R.drawable.ic_search)
        binding.imageEndIconDrawable.setOnClickListener { }
    }

    private fun updateResultText(count: Int) {
        binding.textResult.text = if (count > 0) {
            "Найдено $count"
        } else {
            getString(R.string.no_vacancies)
        }
    }

    private fun openVacancy(id: String) {

    }

    private fun showContent(data: List<Vacancy>, paging: Boolean) {
        adapter.setItems(data)
        if (!paging) {
            binding.recyclerViewVacancies.scrollToPosition(0)
        }
        setListVisibility(true)
        setResultVisibility(true)
        setProgressVisibility(false)
        setStartVisibility(false)
        setErrorVisibility(false)
        setPagingProgressVisibility(false)
    }

    private fun showPageLoading() {
        binding.recyclerViewVacancies.scrollToPosition(adapter.itemCount - 1)
        setListVisibility(true)
        setResultVisibility(true)
        setProgressVisibility(false)
        setStartVisibility(false)
        setErrorVisibility(false)
        setPagingProgressVisibility(true)
    }

    private fun showLoading() {
        setProgressVisibility(true)
        setListVisibility(false)
        setResultVisibility(false)
        setStartVisibility(false)
        setErrorVisibility(false)
        setPagingProgressVisibility(false)
    }

    private fun showError(errorType: ErrorType) {
        setStartVisibility(false)
        setProgressVisibility(false)
        setListVisibility(false)
        setErrorVisibility(true)
        setPagingProgressVisibility(false)

        when (errorType) {
            ErrorType.EMPTY -> {
                binding.imageInfo.setImageResource(R.drawable.empty_results_cat)
                binding.textInfo.text = getString(R.string.can_not_get_vacancies)
                setResultVisibility(true)
            }

            ErrorType.NO_CONNECTION -> {
                binding.imageInfo.setImageResource(R.drawable.skull)
                binding.textInfo.text = getString(R.string.no_internet_connection)
                setResultVisibility(false)
            }

            ErrorType.SERVER_ERROR -> {
                binding.imageInfo.setImageResource(R.drawable.search_server_error)
                binding.textInfo.text = getString(R.string.server_error)
                setResultVisibility(false)
            }
        }

    }

    private fun showStart() {
        setResultVisibility(false)
        setProgressVisibility(false)
        setListVisibility(false)
        setPagingProgressVisibility(false)
        binding.textInfo.isVisible = false
        binding.imageInfo.isVisible = true
        binding.imageInfo.setImageResource(R.drawable.search_screen_placeholder)
    }

    private fun setStartVisibility(isVisible: Boolean) {
        binding.imageInfo.isVisible = isVisible
    }

    private fun setErrorVisibility(isVisible: Boolean) {
        binding.textInfo.isVisible = isVisible
        binding.imageInfo.isVisible = isVisible
    }

    private fun setListVisibility(isVisible: Boolean) {
        binding.recyclerViewVacancies.isVisible = isVisible
    }

    private fun setResultVisibility(isVisible: Boolean) {
        binding.textResult.isVisible = isVisible

    }

    private fun setProgressVisibility(isVisible: Boolean) {
        binding.progressBar.isVisible = isVisible
    }

    private fun setPagingProgressVisibility(isVisible: Boolean) {
        binding.progressBarPaging.isVisible = isVisible
    }


    companion object {
        const val VACANCY_KEY = "vacancy"
    }



}
