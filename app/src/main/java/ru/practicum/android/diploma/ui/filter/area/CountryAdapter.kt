package ru.practicum.android.diploma.ui.filter.area

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class CountryAdapter(
    context: Context,
    private val countries: List<String>
) : ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, countries) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_dropdown_item_1line, parent, false)

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = getItem(position)

        return view
    }
}
