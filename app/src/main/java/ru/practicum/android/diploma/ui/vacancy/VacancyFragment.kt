package ru.practicum.android.diploma.ui.vacancy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.imageview.ShapeableImageView
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyBinding
import ru.practicum.android.diploma.domain.models.Vacancy
import ru.practicum.android.diploma.presentation.vacancy.VacancyViewModel

@Suppress("DEPRECATION")
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

        //Загрузка текстовой информации и лого из параметров переданной Вакансии
        binding.apply {
            loadVacancyLogo(requireContext(),vacancy.employerLogo, vacancyLogo)
            vacancyName.text = vacancy.name
            vacancySalary.text = formatSalary(vacancy.salaryFrom, vacancy.salaryTo, vacancy.salaryCurrency)
            vacancySphere.text = formatCompanyName(vacancy.employerName, vacancy.industry.name)
            vacancyRegion.text = formatAddress(vacancy.addressCity, vacancy.addressStreet, vacancy.addressBuilding)
            vacancyYear.text = formatExperience(vacancy.experience, vacancy.schedule, vacancy.employment)
            vacancyDescription.text = vacancy.description
            vacancySkillsList.text = formatSkills(vacancy.skills)
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

    private fun loadVacancyLogo(context: Context, imageUrl: String?, imageView: ShapeableImageView) {
        if (imageUrl.isNullOrEmpty()) {
            imageView.setImageResource(R.drawable.ic_placeholder)
            return
        }

        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_placeholder)
            .into(imageView)
    }

    private fun formatSalary(salaryFrom: Int?, salaryTo: Int?, currencyCode: String?): String {

        fun getCurrencySymbol(code: String?): String = when (code?.uppercase()) {
            "RUB" -> "₽"
            "USD", "NZD", "SGD" -> "$"
            "EUR" -> "€"
            "GBP" -> "£"
            "JPY" -> "¥"
            "CNY" -> "¥"
            "KZT" -> "₸"
            "UAH" -> "₴"
            else -> code ?: ""
        }

        fun Int?.formatWithSpaces(): String = when {
            this == null -> ""
            this == 0 -> ""
            else -> "%,d".format(this).replace(',', ' ')
        }

        val currencySymbol = getCurrencySymbol(currencyCode)
        val from = salaryFrom.formatWithSpaces()
        val to = salaryTo.formatWithSpaces()

        return when {
            from.isNotEmpty() && to.isNotEmpty() -> "от $from $currencySymbol до $to $currencySymbol"
            from.isNotEmpty() -> "от $from $currencySymbol"
            to.isNotEmpty() -> "до $to $currencySymbol"
            else -> "Уровень зарплаты не указан"
        }
    }

    private fun formatCompanyName(employerName: String?, industry: String?): String {
        return employerName?.takeIf { it.isNotBlank() } ?: industry?.takeIf { it.isNotBlank() } ?: ""
    }

    private fun formatAddress(addressCity: String, addressStreet: String?, addressBuilding: String?): String {
        val streetAndBuilding = listOfNotNull(addressStreet, addressBuilding)
            .takeIf { it.isNotEmpty() }
            ?.joinToString(", ")

        return listOfNotNull(streetAndBuilding, addressCity)
            .joinToString(", ")
    }

    private fun formatExperience(experience: String, schedule: String, employment: String): String {
        val secondLine = listOfNotNull(
            schedule.takeIf { it.isNotBlank() },
            employment.takeIf { it.isNotBlank() }
        ).joinToString(", ")

        return listOfNotNull(
            experience.takeIf { it.isNotBlank() },
            secondLine.takeIf { it.isNotBlank() }
        ).joinToString("\n")
    }

    private fun formatSkills(skills: List<String>): String {
        if (skills.isEmpty()) {
            binding.vacancySkills.visibility = View.GONE
            return ""
        }
        return skills.joinToString("\n") { " • $it" }
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

