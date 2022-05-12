package com.keridano.soccersim.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.keridano.soccersim.R
import com.keridano.soccersim.model.Group
import com.keridano.soccersim.model.Team

/**
 * Adapter used to represent the [Group] rankings header in a [RecyclerView].
 * This will always be represented with a single item (the header).
 * To be connected with the [GroupStandingsAdapter] using a [ConcatAdapter]
 */
class GroupStandingsHeaderAdapter : RecyclerView.Adapter<GroupStandingsHeaderViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroupStandingsHeaderViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.group_standings_header,
            parent, false
        )
        return GroupStandingsHeaderViewHolder(itemView, parent.context)
    }

    override fun onBindViewHolder(holder: GroupStandingsHeaderViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount() = 1 // it is a single item
}

/**
 * ViewHolder of the [GroupStandingsHeaderAdapter].
 */
class GroupStandingsHeaderViewHolder(
    headerView: View,
    private val context: Context
) : RecyclerView.ViewHolder(headerView) {
    private var teamHeader: TextView
    private var wonMatchesHeader: TextView
    private var drawMatchesHeader: TextView
    private var lostMatchesHeader: TextView
    private var scoredGoalsHeader: TextView
    private var concededGoalsHeader: TextView
    private var goalDifferenceHeader: TextView
    private var pointsHeader: TextView

    init {
        headerView.apply {
            teamHeader = findViewById(R.id.team_header_tw)
            wonMatchesHeader = findViewById(R.id.won_matches_header_tw)
            drawMatchesHeader = findViewById(R.id.draw_matches_header_tw)
            lostMatchesHeader = findViewById(R.id.lost_matches_header_tw)
            scoredGoalsHeader = findViewById(R.id.scored_goals_header_tw)
            concededGoalsHeader = findViewById(R.id.conceded_goals_header_tw)
            goalDifferenceHeader = findViewById(R.id.goal_difference_header_tw)
            pointsHeader = findViewById(R.id.points_header_tw)
        }
    }

    fun bind() {
        teamHeader.text = context.getString(R.string.group_standings_header_team)
        wonMatchesHeader.text = context.getString(R.string.group_standings_header_won)
        drawMatchesHeader.text = context.getString(R.string.group_standings_header_draw)
        lostMatchesHeader.text = context.getString(R.string.group_standings_header_lost)
        scoredGoalsHeader.text = context.getString(R.string.group_standings_header_scored)
        concededGoalsHeader.text = context.getString(R.string.group_standings_header_conceded)
        goalDifferenceHeader.text = context.getString(R.string.group_standings_header_diff)
        pointsHeader.text = context.getString(R.string.group_standings_header_points)
    }
}