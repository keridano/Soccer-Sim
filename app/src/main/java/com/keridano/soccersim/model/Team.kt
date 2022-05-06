package com.keridano.soccersim.model

/**
 * A football [Team] with his info and scores
 *
 * @param name the team name
 * @param strength the team strength (used to calculate the matches results). Default value for every team is 50
 * @param logo ID of the res file used for the logo. In order to extend this, this could be an URL to be fetched using Picasso library
 * @param wonMatches number of matches won by the team in the Group stage
 * @param lostMatches number of matches lost by the team in the Group stage
 * @param drawMatches number of matches resulted in a draw in the Group stage
 * @param scoredGoals number of goals scored by the team in the Group stage
 * @param concededGoals number of goals conceded by the team in the Group stage
 */
data class Team(
    val name: String,
    val logo: Int,
    var strength: Int = 50,
    var wonMatches: Int = 0,
    var lostMatches: Int = 0,
    var drawMatches: Int = 0,
    var scoredGoals: Int = 0,
    var concededGoals: Int = 0
)

fun Team.getCurrentGoalDifference(): Int {
    return scoredGoals - concededGoals
}

fun Team.getCurrentPoints(): Int {
    return (wonMatches * 3) + drawMatches
}
