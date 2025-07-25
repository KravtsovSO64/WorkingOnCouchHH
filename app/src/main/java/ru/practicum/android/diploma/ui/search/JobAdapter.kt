package ru.practicum.android.diploma.ui.search

import android.content.ClipData
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ItemJobBinding
import ru.practicum.android.diploma.domain.models.Vacancy

class JobAdapter (
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
                vacancyEmployerTextView.text = employer
                vacancySalaryTextView.text = salaryFrom.toString()+"  "+salaryTo.toString()
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
            //В Vacancy id нужен для перехода в подробности
            //holder.itemView.setOnClickListener { onItemClick(this.id) }
        }
    }

    fun setItems(items: List<Vacancy>) {
        clear()
        vacancies.addAll(items)
        notifyDataSetChanged()
    }

    private fun clear() {
        vacancies.clear()
    }
}
