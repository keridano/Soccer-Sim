package com.keridano.soccersim.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ConcatAdapter
import com.keridano.soccersim.R
import com.keridano.soccersim.databinding.MainFragmentBinding
import com.keridano.soccersim.model.Team

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var groupStandingsAdapter: GroupStandingsAdapter
    private lateinit var groupStandingsHeaderAdapter: GroupStandingsHeaderAdapter
    private lateinit var groupStandingsConcatAdapter: ConcatAdapter

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
        groupStandingsHeaderAdapter = GroupStandingsHeaderAdapter()
        groupStandingsAdapter = GroupStandingsAdapter()
        groupStandingsAdapter.setItems(createTeams())
        groupStandingsConcatAdapter = ConcatAdapter(groupStandingsHeaderAdapter, groupStandingsAdapter)
        binding.groupStandingsRw.adapter = groupStandingsConcatAdapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun createTeams(): List<Team> {
        return listOf(
            Team(name = "France", logo = R.drawable.ic_france_logo, strength = 60),
            Team(name = "Spain", logo = R.drawable.ic_spain_logo, strength = 55),
            Team(name = "Belgium", logo = R.drawable.ic_belgium_logo, strength = 50),
            Team(name = "Finland", logo = R.drawable.ic_finland_logo, strength = 45),
        )
    }
}