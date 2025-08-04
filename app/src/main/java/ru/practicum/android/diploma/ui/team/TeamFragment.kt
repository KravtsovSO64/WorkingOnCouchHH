package ru.practicum.android.diploma.ui.team

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import org.koin.android.ext.android.inject
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentTeamBinding
import ru.practicum.android.diploma.ui.team.models.TeamMember
import ru.practicum.android.diploma.util.viewBinding

class TeamFragment : Fragment(R.layout.fragment_team), SwipeStack.SwipeStackListener {

    private val binding by viewBinding<FragmentTeamBinding>()
    private val swipeAdapter: SwipeAdapter by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val list = getImgData()
        swipeAdapter.mData.addAll(list)
        binding.swipeStack.adapter = swipeAdapter
        binding.swipeStack.setListener(this)
    }

    private fun getImgData(): List<TeamMember> {
        val list = mutableListOf<TeamMember>()
        list.add(
            TeamMember(
                photo = R.drawable.team_image,
                name = getString(R.string.team_1),
                description = "Тот, кто знает, где спрятана магия."
            )
        )
        list.add(
            TeamMember(
                photo = R.drawable.team_image,
                name = getString(R.string.team_2),
                description = "Мастер структуры и скрытых связей."
            )
        )
        list.add(
            TeamMember(
                photo = R.drawable.team_image,
                name = getString(R.string.team_3),
                description = "Видит форму там, где другие видят пустоту."
            )
        )
        list.add(
            TeamMember(
                photo = R.drawable.team_image,
                name = getString(R.string.team_4),
                description = "Разгадывает то, что другие даже не замечают."
            )
        )
        return list
    }

    override fun onViewSwipedToLeft(position: Int) = Unit
    override fun onViewSwipedToRight(position: Int) = Unit
    override fun onStackEmpty() = Unit
}
