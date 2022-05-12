package com.keridano.soccersim.model.enum

import com.keridano.soccersim.model.Match
import com.keridano.soccersim.model.Team

/**
 * A series of [Bonus] which gives perks to a [Team]
 */
enum class Bonus {
    /**
     * Gives 10 bonus points to [Team] strength
     */
    BEST_PLAYERS,

    /**
     * Removes 1 conceded Goal in the [Match] simulation
     */
    BEST_DEFENSE,

    /**
     * Gives 5 Bonus points to [Team] strength
     */
    BEST_COACH,

    /**
     * The [Match] simulation will use a (10..50) [IntRange.random] range for this [Team]
     * instead of the default (0..40) [IntRange.random] range
     */
    LUCKY_TEAM
}