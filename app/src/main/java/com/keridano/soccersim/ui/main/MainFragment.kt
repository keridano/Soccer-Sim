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
import com.keridano.soccersim.databinding.MainFragmentBinding
import com.keridano.soccersim.extension.TAG
import com.keridano.soccersim.extension.prettyPrint
import com.keridano.soccersim.model.Group
import com.keridano.soccersim.ui.main.adapter.GroupStandingsAdapter
import com.keridano.soccersim.ui.main.adapter.GroupStandingsHeaderAdapter
import com.keridano.soccersim.ui.main.adapter.ResultsAdapter
import com.keridano.soccersim.util.Logger
import com.keridano.soccersim.util.RecyclerViewUiState

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

    private val logger = Logger(TAG)

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
        groupStandingsConcatAdapter = ConcatAdapter(
            groupStandingsHeaderAdapter,
            groupStandingsAdapter
        )

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
            viewModel.startSimulation()
        }

        viewModel.uiState.observe(viewLifecycleOwner) {
            onUiState(it)
        }

        viewModel.group.observe(viewLifecycleOwner) {
            onGroupChanges(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.startButton.setOnClickListener(null)
        _binding = null
    }

    private fun onUiState(it: MainViewUiState) {
        logger.d("a change in the UiState occurred: ${it.prettyPrint()}")
        binding.groupResultsRw.stateView = when {
            it.isSpinnerBlockingUi -> RecyclerViewUiState.LOADING
            resultsAdapter.itemCount > 0 -> RecyclerViewUiState.NORMAL
            else -> RecyclerViewUiState.EMPTY
        }

        binding.startButton.isEnabled = it.isStartSimulationButtonActive
    }

    private fun onGroupChanges(it: Group) {
        logger.d("a change in the Group occurred: ${it.prettyPrint()}")
        groupStandingsAdapter.setItems(it.teams)
        resultsAdapter.setItems(it.matches)
    }
}