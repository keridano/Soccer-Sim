@file:Suppress("UNCHECKED_CAST")

package com.keridano.soccersim.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.keridano.soccersim.R
import com.keridano.soccersim.extension.TAG
import com.keridano.soccersim.extension.reassign
import com.keridano.soccersim.model.*
import com.keridano.soccersim.model.enum.Bonus
import com.keridano.soccersim.util.DispatcherProvider
import com.keridano.soccersim.util.DispatcherProviders
import com.keridano.soccersim.util.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(
    private val dispatcherProvider: DispatcherProvider = DispatcherProviders.default
) : ViewModel() {

    private var _group: MutableLiveData<Group> = MutableLiveData(
        Group(
            teams = mutableListOf(
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
                    strength = 40,
                    bonuses = listOf(Bonus.LUCKY_TEAM)
                )
            ),
            matches = mutableListOf()
        )
    )
    val group: LiveData<Group>
        get() = _group

    private var _uiState: MutableLiveData<MainViewUiState> = MutableLiveData(MainViewUiState())
    val uiState: LiveData<MainViewUiState>
        get() = _uiState

    private val logger = Logger(TAG)

    fun startSimulation() {
        CoroutineScope(dispatcherProvider.background).launch startSimulation@{
            startLoading()
            delay(2000) //simulating an API call
            createMatches()
            simulateMatches()
            stopLoading()
        }
    }

    private fun startLoading() {
        logger.d("started loading!")
        _uiState.reassign {
            it.spinnerVisible(true).startSimulationButtonActive(false)
        }
    }

    private fun createMatches() {
        logger.d("creating matches...")
        group.value?.teams?.let { teams ->
            group.value?.matches?.let { matches ->
                // reset old results and matches
                teams.forEach { it.clearAllResults() }
                matches.clear()

                val firstTeam = teams[0]
                var matchDay = teams.size - 1

                while (matchDay > 0) {
                    val teamsCopy = mutableListOf<Team>()
                    teamsCopy.addAll(teams)
                    val secondTeam = teamsCopy[matchDay]
                    teamsCopy.removeAt(matchDay)

                    // distribute teams using a coin flip to choose the away and home teams
                    // This wont distribute evenly
                    // (it can happen for a team to be always the away team or vice versa)
                    val coinFlip = listOf(true, false).shuffled().first()
                    if (coinFlip)
                        matches.add(
                            Match(
                                homeTeam = secondTeam,
                                awayTeam = firstTeam,
                                matchDay = matchDay
                            )
                        )
                    else
                        matches.add(
                            Match(
                                homeTeam = firstTeam,
                                awayTeam = secondTeam,
                                matchDay = matchDay
                            )
                        )

                    // remove the firstTeam
                    teamsCopy.removeFirst()

                    // create the second match with the other two teams
                    if (coinFlip)
                        matches.add(
                            Match(
                                homeTeam = teamsCopy[1],
                                awayTeam = teamsCopy[0],
                                matchDay = matchDay
                            )
                        )
                    else
                        matches.add(
                            Match(
                                homeTeam = teamsCopy[0],
                                awayTeam = teamsCopy[1],
                                matchDay = matchDay
                            )
                        )

                    matchDay -= 1
                }

                matches.sortBy { it.matchDay }
                _group.postValue(Group(teams, matches))
            }
        }
    }

    private fun simulateMatches() {
        group.value?.teams?.let { teams ->
            group.value?.matches?.let { matches ->
                matches.forEach { match ->
                    match.simulateMatch()
                    teams.firstOrNull {
                        it == match.homeTeam
                    }?.let { home ->
                        home.scoredGoals += match.homeTeamGoals
                        home.concededGoals += match.awayTeamGoals

                        when {
                            match.homeTeamGoals > match.awayTeamGoals -> home.wonMatches += 1
                            match.homeTeamGoals == match.awayTeamGoals -> home.drawMatches += 1
                            else -> home.lostMatches += 1
                        }
                    }

                    teams.firstOrNull {
                        it == match.awayTeam
                    }?.let { away ->
                        away.scoredGoals += match.awayTeamGoals
                        away.concededGoals += match.homeTeamGoals

                        when {
                            match.awayTeamGoals > match.homeTeamGoals -> away.wonMatches += 1
                            match.awayTeamGoals == match.homeTeamGoals -> away.drawMatches += 1
                            else -> away.lostMatches += 1
                        }
                    }
                }

                _group.postValue(
                    Group(teams.sortedWith(
                        compareByDescending<Team> { it.getCurrentPoints() }
                            .thenByDescending { it.getCurrentGoalDifference() }
                            .thenByDescending { it.scoredGoals }
                            .thenByDescending { it.concededGoals }
                    ).toMutableList(),
                        matches
                    )
                )
            }
        }
    }

    private fun stopLoading() {
        logger.d("stopped loading!")
        _uiState.reassign {
            it.spinnerVisible(false).startSimulationButtonActive(true)
        }
    }
}

class MainViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel() as T
    }
}