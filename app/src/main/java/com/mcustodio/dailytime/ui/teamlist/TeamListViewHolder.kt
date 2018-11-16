package com.mcustodio.dailytime.ui.teamlist

import android.support.v7.widget.RecyclerView
import android.view.View
import com.mcustodio.dailytime.data.Team
import kotlinx.android.synthetic.main.item_teamlistitem_team.view.*

class TeamListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val textName = view.text_teamlist

    fun setValues(team: Team) {
        textName.text = team.name
    }
}