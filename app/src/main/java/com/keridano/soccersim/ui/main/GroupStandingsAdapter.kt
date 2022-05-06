package com.keridano.soccersim.ui.main

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.keridano.soccersim.R
import com.keridano.soccersim.model.Team
import com.keridano.soccersim.model.getCurrentGoalDifference
import com.keridano.soccersim.model.getCurrentPoints

@SuppressLint("NotifyDataSetChanged")
class GroupStandingsAdapter : RecyclerView.Adapter<GroupStandingsViewHolder>() {

    private val teams = mutableListOf<Team>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupStandingsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.group_standings_item,
            parent, false
        )
        return GroupStandingsViewHolder(itemView, parent.context)
    }

    override fun onBindViewHolder(holder: GroupStandingsViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    override fun getItemCount() = teams.size

    fun setItems(newItems: List<Team>) {
        teams.clear()
        teams.addAll(newItems)
        notifyDataSetChanged()
    }

    private fun getItem(position: Int) = teams.elementAt(position)
}

@SuppressLint("SetTextI18n")
class GroupStandingsViewHolder(
    itemView: View,
    private val context: Context
) : RecyclerView.ViewHolder(itemView) {
    private var position: TextView
    private var logo: ImageView
    private var name: TextView
    private var wonMatches: TextView
    private var drawMatches: TextView
    private var lostMatches: TextView
    private var scoredGoals: TextView
    private var concededGoals: TextView
    private var goalDifference: TextView
    private var points: TextView

    private var boundItem: Team? = null

    init {
        itemView.apply {
            position = findViewById(R.id.position_tw)
            logo = findViewById(R.id.logo_iw)
            name = findViewById(R.id.name_tw)
            wonMatches = findViewById(R.id.won_matches_tw)
            drawMatches = findViewById(R.id.draw_matches_tw)
            lostMatches = findViewById(R.id.lost_matches_tw)
            scoredGoals = findViewById(R.id.scored_goals_tw)
            concededGoals = findViewById(R.id.conceded_goals_tw)
            goalDifference = findViewById(R.id.goal_difference_tw)
            points = findViewById(R.id.points_tw)
        }
    }

    fun bind(item: Team, pos: Int) {
        item.also {
            position.text = "${pos + 1}"
            logo.setImageDrawable(ContextCompat.getDrawable(context, it.logo))
            name.text = it.name
            wonMatches.text = it.wonMatches.toString()
            drawMatches.text = it.drawMatches.toString()
            lostMatches.text = it.lostMatches.toString()
            scoredGoals.text = it.scoredGoals.toString()
            concededGoals.text = it.concededGoals.toString()
            goalDifference.text = it.getCurrentGoalDifference().toString()
            points.text = it.getCurrentPoints().toString()
            boundItem = it
        }
    }
}