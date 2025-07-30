package ru.practicum.android.diploma.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentFavouriteBinding
import ru.practicum.android.diploma.domain.models.VacancyDetail
import ru.practicum.android.diploma.presentation.favourites.FavouritesViewModel
import ru.practicum.android.diploma.presentation.favourites.state.FavouritesState
import kotlin.getValue

class FavouriteFragment : Fragment() {

    private var _binding: FragmentFavouriteBinding? = null
    private val binding get() = _binding!!

    private val favouritesViewModel: FavouritesViewModel by viewModel()

    private var adapterFavourites = FavouriteVacancyAdapter { item ->

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        favouritesViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }

        binding.recyclerViewFavoriteVacancies.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewFavoriteVacancies.adapter = adapterFavourites

        favouritesViewModel.getFavourites()
    }

    private fun render(state: FavouritesState) {
        when (state) {
            is FavouritesState.Empty -> {
                showEmpty()
            }

            is FavouritesState.Loading -> {
                showLoading()
            }

            is FavouritesState.Favorites -> {
                showFavorites(state.vacancies)
            }
        }
    }

    private fun showEmpty() {
        binding.recyclerViewFavoriteVacancies.isVisible = false
        binding.favouriteEmpty.isVisible = true
        binding.favouriteError.isVisible = false
    }

    private fun showLoading() {
        binding.recyclerViewFavoriteVacancies.isVisible = false
        binding.favouriteEmpty.isVisible = false
        binding.favouriteError.isVisible = true
    }

    private fun showFavorites(list: List<VacancyDetail>) {
        adapterFavourites.setItems(list)
        binding.recyclerViewFavoriteVacancies.isVisible = true
        binding.favouriteEmpty.isVisible = false
        binding.favouriteError.isVisible = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
