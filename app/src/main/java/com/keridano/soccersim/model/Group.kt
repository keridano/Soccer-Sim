package com.keridano.soccersim.model

/**
 * A [Group] of [Team]s that will clash in a series of [Match]es to win the group stage
 *
 * @param teams the teams in the competition
 * @param matches the matches to play between the teams
 */
data class Group(
    val teams: List<Team>,
    val matches: List<Match>
)
