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

/**
 * ViewModel of the [MainFragment].
 * Its public functions are called by the [MainFragment] in order to elaborate some logic
 * (matches creations and simulation) onto the [Group] model
 * and then propagate its changes back to the [MainFragment]
 */
class MainViewModel(
    private val dispatcherProvider: DispatcherProvider = DispatcherProviders.default
) : ViewModel() {

    private var _group: MutableLiveData<Group> = MutableLiveData(
        Group(
            teams = mutableListOf(
                Team(
                    name = "France",
                    logo = R.drawable.ic_france_logo,
                    bonuses = listOf(Bonus.BEST_PLAYERS, Bonus.BEST_DEFENSE)
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

    /**
     * Start a new [Group] simulation adding a small delay to simulate a fake API call
     */
    fun startSimulation() {
        CoroutineScope(dispatcherProvider.background).launch startSimulation@{
            startLoading()
            delay(1000) //simulating an API call
            createMatches()
            simulateMatches()
            stopLoading()
        }
    }

    /**
     * Propagate to the UI the loading state
     */
    private fun startLoading() {
        logger.d("started loading!")
        _uiState.reassign {
            it.spinnerVisible(true).startSimulationButtonActive(false)
        }
    }

    /**
     * Creates the [Match]es between the [Group] [Team]s
     */
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
                    // Pay attention: this wont distribute evenly!
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
                _group.reassign { Group(teams, matches) }
            }
        }
    }

    /**
     * Simulate the [Match]es between the [Group] [Team]s
     */
    private fun simulateMatches() {
        logger.d("simulating matches...")
        group.value?.teams?.let { teams ->
            group.value?.matches?.let { matches ->
                matches.forEach { match ->
                    // Simulate the match
                    match.simulateMatch()
                    // Then updates the home team statistics
                    updateHomeTeamStatistics(teams, match)
                    // And the away team statistics
                    updateAwayTeamStatistics(teams, match)
                }

                logger.d("sort group standings and post the values")
                _group.reassign {
                    Group(teams.sortedWith(
                        compareByDescending<Team> { it.getCurrentPoints() }
                            .thenByDescending { it.getCurrentGoalDifference() }
                            .thenByDescending { it.scoredGoals }
                            .thenByDescending { it.concededGoals }
                            .thenDescending { t, t2 -> // Head to head result
                                // get the match played between the two teams
                                val match = matches.first {
                                    (it.homeTeam == t && it.awayTeam == t2) ||
                                            (it.homeTeam == t2 && it.awayTeam == t)
                                }

                                // sort based on the scored goals.
                                // If it was a tie do not sort anymore
                                when {
                                    match.homeTeamGoals > match.awayTeamGoals -> if (match.homeTeam == t) 1 else -1
                                    match.homeTeamGoals < match.awayTeamGoals -> if (match.homeTeam == t) -1 else 1
                                    else -> 0
                                }
                            }
                    ).toMutableList(),
                        matches
                    )
                }
            }
        }
    }

    /**
     * updates the home [Team] statistics based on the match results
     */
    private fun updateHomeTeamStatistics(
        teams: MutableList<Team>,
        match: Match
    ) {
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
    }

    /**
     * updates the away [Team] statistics based on the match results
     */
    private fun updateAwayTeamStatistics(
        teams: MutableList<Team>,
        match: Match
    ) {
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

    /**
     * Propagate to the UI the completed state
     */
    private fun stopLoading() {
        logger.d("stopped loading!")
        _uiState.reassign {
            it.spinnerVisible(false).startSimulationButtonActive(true)
        }
    }
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel() as T
    }
}