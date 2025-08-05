package ru.practicum.android.diploma.ui.search

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemJobBinding
import ru.practicum.android.diploma.domain.models.Vacancy

class JobAdapter(
    private val onItemClick: ((String) -> Unit)
) : RecyclerView.Adapter<JobAdapter.ViewHolder>() {

    private var vacancies: MutableList<Vacancy> = mutableListOf()

    inner class ViewHolder(private val binding: ItemJobBinding) : RecyclerView.ViewHolder(binding.root) {
        private val logo = binding.imageVacancyLogo
        private val vacancyNameTextView = binding.textVacancyName
        private val vacancyEmployerTextView = binding.textVacancyEmployer
        private val vacancySalaryTextView = binding.textVacancySalary
        fun bind(vacancy: Vacancy) {
            with(vacancy) {
                Glide.with(binding.root)
                    .load(employerLogo)
                    .placeholder(R.drawable.ic_placeholder)
                    .into(logo)
                vacancyNameTextView.text = name
                vacancyEmployerTextView.text = employerName
                vacancySalaryTextView.text = formatSalary(vacancy.salaryFrom, vacancy.salaryTo, vacancy.salaryCurrency)
                itemView.setOnClickListener {
                    onItemClick(vacancy.id)
                }
            }
        }
    }

    override fun getItemCount(): Int = vacancies.size
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemJobBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        with(vacancies[position]) {
            holder.bind(this)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItems(items: List<Vacancy>) {
        clear()
        vacancies.addAll(items)
        notifyDataSetChanged()
    }

    private fun clear() {
        vacancies.clear()
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
}
