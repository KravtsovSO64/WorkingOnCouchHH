package ru.practicum.android.diploma.ui.vacancy

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.vacancy.VacancyViewModel

class VacancyFragment : Fragment() {

    private var _binding: FragmentVacancyBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VacancyViewModel by viewModel()
    private val vacancy by lazy {
        requireArguments().getParcelable<Vacancy>("vacancy")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVacancyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        startObserving() //Настройка наблюдателей
        clickHandler()  //Обработка всех кнопок экрана
        startScreen()  //Настройка экрана Вакансии
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        showBottomNavigation(true)
    }

    private fun startScreen() {

        //Отключаем нижнюю панель навигации
        showBottomNavigation(false)

        //Загрузка текстовой информации из параметров переданной Вакансии
        binding.apply {
            vacancyName.text = vacancy.name
            vacancySalary.text = formatSalary(vacancy.salaryFrom, vacancy.salaryTo, vacancy.salaryCurrency)
        }

        //Проверка Вакансии на наличие в списке избранного
        viewModel.checkJobInFavourites(vacancy.id)
    }

    private fun clickHandler() {
        binding.apply {
            vacancyToolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }

            favouriteButton.setOnClickListener {
                if (it.isSelected) {
                    viewModel.removeFromFavourites(vacancy.id)
                } else {
                    viewModel.addToFavourite(vacancy)
                }
            }

            shareButton.setOnClickListener {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, vacancy.url)
                    putExtra(Intent.EXTRA_SUBJECT, "Посмотри эту Вакансию")
                }
                startActivity(Intent.createChooser(shareIntent, "Поделиться через"))
            }
        }
    }

    private fun startObserving() {
        viewModel.stateFavourite.observe(viewLifecycleOwner) { isFavourite ->
            binding.favouriteButton.isSelected = isFavourite
        }
    }

    private fun formatSalary(salaryFrom: Int?, salaryTo: Int?, currencyCode: String?): String {
        val currencySymbol = getCurrencySymbol(currencyCode)

        fun formatAmount(amount: Int?): String {
            return amount?.takeIf { it != 0 }?.formatWithSpaces() ?: return ""
        }

        val from = formatAmount(salaryFrom)
        val to = formatAmount(salaryTo)

        return when {
            from.isNotEmpty() && to.isNotEmpty() -> "от $from $currencySymbol до $to $currencySymbol"
            from.isNotEmpty() -> "от $from $currencySymbol"
            to.isNotEmpty() -> "до $to $currencySymbol"
            else -> "Уровень зарплаты не указан"
        }
    }

    private fun Int?.formatWithSpaces(): String {
        if (this == null) return ""
        return "%,d".format(this).replace(',', ' ')
    }

    private fun getCurrencySymbol(currencyCode: String?): String {
        return when (currencyCode?.uppercase()) {
            "RUB" -> "₽"
            "USD" -> "$"
            "EUR" -> "€"
            "GBP" -> "£"
            "JPY" -> "¥"
            "CNY" -> "¥"
            "KZT" -> "₸"
            "UAH" -> "₴"
            else -> currencyCode ?: ""
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

