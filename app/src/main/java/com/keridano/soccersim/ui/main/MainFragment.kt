package com.keridano.soccersim.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import com.keridano.soccersim.R
import com.keridano.soccersim.databinding.MainFragmentBinding
import com.keridano.soccersim.model.Match
import com.keridano.soccersim.model.Team
import com.keridano.soccersim.model.enum.Bonus
import com.keridano.soccersim.ui.main.adapter.GroupStandingsAdapter
import com.keridano.soccersim.ui.main.adapter.GroupStandingsHeaderAdapter
import com.keridano.soccersim.ui.main.adapter.ResultsAdapter


class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var groupStandingsAdapter: GroupStandingsAdapter
    private lateinit var groupStandingsHeaderAdapter: GroupStandingsHeaderAdapter
    private lateinit var groupStandingsConcatAdapter: ConcatAdapter
    private lateinit var resultsAdapter: ResultsAdapter

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // We don't need a factory here! this will return the same view model
        // already created for MainActivity
        viewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dividerItemDecoration = DividerItemDecoration(
            requireContext(),
            LinearLayout.VERTICAL
        )

        groupStandingsHeaderAdapter = GroupStandingsHeaderAdapter()
        groupStandingsAdapter = GroupStandingsAdapter()
        groupStandingsAdapter.setItems(createTeams())
        groupStandingsConcatAdapter =
            ConcatAdapter(groupStandingsHeaderAdapter, groupStandingsAdapter)
        binding.groupStandingsRw.adapter = groupStandingsConcatAdapter
        binding.groupStandingsRw.addItemDecoration(dividerItemDecoration)

        resultsAdapter = ResultsAdapter()
        binding.groupResultsRw.apply {
            emptyStateView = binding.emptyView
            loadingStateView = binding.loadingView
            adapter = resultsAdapter
            addItemDecoration(dividerItemDecoration)
        }

        binding.startButton.setOnClickListener {
            // FIXME for test only
            resultsAdapter.setItems(
                listOf(
                    Match(
                        homeTeam = Team(
                            name = "France",
                            logo = R.drawable.ic_france_logo,
                            bonuses = listOf(Bonus.BEST_PLAYERS)
                        ),
                        homeTeamGoals = 3,
                        awayTeam = Team(
                            name = "Spain",
                            logo = R.drawable.ic_spain_logo,
                            bonuses = listOf(Bonus.BEST_COACH)
                        ),
                        awayTeamGoals = 2,
                        matchDay = 1
                    ),
                    Match(
                        homeTeam = Team(
                            name = "Belgium",
                            logo = R.drawable.ic_belgium_logo,
                            bonuses = listOf(Bonus.BEST_DEFENSE)
                        ),
                        homeTeamGoals = 1,
                        awayTeam = Team(
                            name = "Finland",
                            logo = R.drawable.ic_finland_logo,
                            bonuses = listOf(Bonus.LUCKY_TEAM)
                        ),
                        awayTeamGoals = 1,
                        matchDay = 2
                    )
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createTeams(): List<Team> {
        return listOf(
            Team(
                name = "France",
                logo = R.drawable.ic_france_logo,
                bonuses = listOf(Bonus.BEST_PLAYERS)
            ),
            Team(
                name = "Spain",
                logo = R.drawable.ic_spain_logo,
                bonuses = listOf(Bonus.BEST_COACH)
            ),
            Team(
                name = "Belgium",
                logo = R.drawable.ic_belgium_logo,
                bonuses = listOf(Bonus.BEST_DEFENSE)
            ),
            Team(
                name = "Finland",
                logo = R.drawable.ic_finland_logo,
                bonuses = listOf(Bonus.LUCKY_TEAM)
            ),
        )
    }
}