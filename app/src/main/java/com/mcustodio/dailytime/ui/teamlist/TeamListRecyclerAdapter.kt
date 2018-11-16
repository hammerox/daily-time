package com.mcustodio.dailytime.ui.teamlist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mcustodio.dailytime.R
import com.mcustodio.dailytime.data.Team

class TeamListRecyclerAdapter(var onItemClick: ((Team) -> Unit)? = null) : RecyclerView.Adapter<TeamListViewHolder>() {

    var teamList: List<Team> = ArrayList()
        set (value) {
            field = value
            notifyDataSetChanged()
        }



    override fun getItemCount(): Int = teamList.size

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): TeamListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_teamlistitem_team, parent, false)
        return TeamListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeamListViewHolder, position: Int) {
        val team = teamList[position]
        holder.setValues(team)
        holder.view.setOnClickListener {
            onItemClick?.invoke(team)
        }
    }

}