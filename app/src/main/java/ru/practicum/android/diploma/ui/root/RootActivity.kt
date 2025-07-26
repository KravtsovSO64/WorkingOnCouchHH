package ru.practicum.android.diploma.ui.root

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.data.network.impl.VacanciesRepositoryImpl
import ru.practicum.android.diploma.databinding.ActivityRootBinding
import ru.practicum.android.diploma.domain.api.VacanciesInteractor
import ru.practicum.android.diploma.domain.impl.VacanciesInteractorImpl
import ru.practicum.android.diploma.domain.models.Vacancy

class RootActivity (): AppCompatActivity() {

    private val intertactor: VacanciesInteractor by inject()

    private var _binding: ActivityRootBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        _binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)



        var pair = Pair(listOf<Vacancy>(), "")

        lifecycleScope.launch {
            intertactor.searchVacancies("UX", 0)
                .collect { item ->
                    pair = Pair(item.first.orEmpty(), item.second.orEmpty())
                }
            Log.i("HH response data", pair.first.toString())
            Log.i("HH response error", pair.second)
        }
    }
}
