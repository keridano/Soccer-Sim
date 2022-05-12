package com.keridano.soccersim.model

import com.keridano.soccersim.model.enum.Bonus

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

/**
 * The [Match] simulation consists in a simple math calculation based on the following rules:
 *
 * - the home team gets 5 bonus points (12th man effect)
 * - every team has got a [Bonus] which gives bonus points or better chances
 * - a Random value, representing the chance, is added to the team strength and bonuses
 */
fun Match.simulateMatch() {

    val homeBonus = 5
    var homeTeamPoints = homeTeam.strength + homeBonus
    var awayTeamPoints = awayTeam.strength

    // add bonus perks
    if (homeTeam.bonuses.contains(Bonus.BEST_PLAYERS)) homeTeamPoints += 10
    if (homeTeam.bonuses.contains(Bonus.BEST_COACH)) homeTeamPoints += 5
    if (awayTeam.bonuses.contains(Bonus.BEST_PLAYERS)) awayTeamPoints += 10
    if (awayTeam.bonuses.contains(Bonus.BEST_COACH)) awayTeamPoints += 5

    homeTeamPoints += if (homeTeam.bonuses.contains(Bonus.LUCKY_TEAM))
        (10..50).random() // The luckiest team has got better chances
    else
        (0..40).random()

    awayTeamPoints += if (awayTeam.bonuses.contains(Bonus.LUCKY_TEAM))
        (10..50).random() // The luckiest team has got better chances
    else
        (0..40).random()

    // calculate the scored goals based on the calculated points
    homeTeamGoals = transformPointsInGoals(homeTeamPoints)
    awayTeamGoals = transformPointsInGoals(awayTeamPoints)

    // remove 1 conceded goal if the team has the best defense Bonus
    if (homeTeam.bonuses.contains(Bonus.BEST_DEFENSE) && homeTeamGoals > 0) homeTeamGoals -= 1
    if (awayTeam.bonuses.contains(Bonus.BEST_DEFENSE) && awayTeamGoals > 0) awayTeamGoals -= 1
}

/**
 * transform points calculated in the simulation in goals using this rule:
 * - values lower than 65 doesn't produce goals
 * - starting from 66 every 5 points results in a scored goal
 * - scored goals cap is 6
 */
fun transformPointsInGoals(points: Int): Int {
    return when (points) {
        in 0..65 -> 0
        in 66..71 -> 1
        in 72..77 -> 2
        in 78..83 -> 3
        in 84..89 -> 4
        in 90..95 -> 5
        else -> 6
    }
}