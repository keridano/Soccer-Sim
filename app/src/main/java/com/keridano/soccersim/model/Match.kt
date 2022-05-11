package com.keridano.soccersim.model

/**
 * A football [Match] between two [Team]s
 *
 * @param homeTeam the home team
 * @param awayTeam the away team
 * @param homeTeamGoals the number of goals scored by the home team
 * @param awayTeamGoals the number of goals scored by the away team
 */
data class Match(
    val homeTeam: Team,
    val awayTeam: Team,
    val matchDay: Int,
    var homeTeamGoals: Int = 0,
    var awayTeamGoals: Int = 0
)