package com.example.firebaserealtimedb.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaserealtimedb.R

class Adapter(private var teams: List<String>) : RecyclerView.Adapter<Adapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewTeamName: TextView = itemView.findViewById(R.id.textViewTeamName)
        private val textViewWins: TextView = itemView.findViewById(R.id.textViewWins)
        private val  textViewLosses: TextView = itemView.findViewById(R.id.textViewLosses)

        fun bind(teamName: String) {
            val data = teamName.split(" - ")
            textViewTeamName.text = data[0]

            val record = data[1].split(", ")
            textViewWins.text = record[0]
            textViewLosses.text = record[1]
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_team, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val teamName = teams[position]
        holder.bind(teamName)
    }

    override fun getItemCount(): Int {
        return teams.size
    }

    fun updateData(newTeams: List<String>) {
        teams = newTeams
        notifyDataSetChanged()
    }
}