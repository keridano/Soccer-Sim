package com.keridano.soccersim.ui.main.adapter

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
import com.keridano.soccersim.model.Match

@SuppressLint("NotifyDataSetChanged")
class ResultsAdapter : RecyclerView.Adapter<ResultsViewHolder>() {

    private val matches = mutableListOf<Match>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.results_item,
            parent, false
        )
        return ResultsViewHolder(itemView, parent.context)
    }

    override fun onBindViewHolder(holder: ResultsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount() = matches.size

    fun setItems(newItems: List<Match>) {
        matches.clear()
        matches.addAll(newItems)
        notifyDataSetChanged()
    }

    private fun getItem(position: Int) = matches.elementAt(position)
}

@SuppressLint("SetTextI18n")
class ResultsViewHolder(
    itemView: View,
    private val context: Context
) : RecyclerView.ViewHolder(itemView) {

    private var homeLogo: ImageView
    private var homeName: TextView
    private var homeScore: TextView
    private var matchDay: TextView
    private var awayLogo: ImageView
    private var awayName: TextView
    private var awayScore: TextView

    private var boundItem: Match? = null

    init {
        itemView.apply {
            homeLogo = findViewById(R.id.home_logo_iw)
            homeName = findViewById(R.id.home_name_tw)
            homeScore = findViewById(R.id.home_score_tw)
            matchDay = findViewById(R.id.match_day_tw)
            awayLogo = findViewById(R.id.away_logo_iw)
            awayName = findViewById(R.id.away_name_tw)
            awayScore = findViewById(R.id.away_score_tw)
        }
    }

    fun bind(item: Match) {
        item.also {
            homeLogo.setImageDrawable(ContextCompat.getDrawable(context, it.homeTeam.logo))
            homeName.text = it.homeTeam.name
            homeScore.text = it.homeTeamGoals.toString()
            matchDay.text = context.getString(R.string.match_day, it.matchDay)
            awayLogo.setImageDrawable(ContextCompat.getDrawable(context, it.awayTeam.logo))
            awayName.text = it.awayTeam.name
            awayScore.text = it.awayTeamGoals.toString()
            boundItem = it
        }
    }
}