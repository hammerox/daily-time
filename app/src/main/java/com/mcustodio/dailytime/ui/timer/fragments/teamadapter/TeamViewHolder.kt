package com.mcustodio.dailytime.ui.timer.fragments.teamadapter

import android.support.v7.widget.RecyclerView
import android.view.View
import com.mcustodio.dailytime.data.Member
import kotlinx.android.synthetic.main.item_timerteam_member.view.*
import java.text.SimpleDateFormat

class TeamViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val textName = view.text_timerteam_name
    private val textTimer = view.text_timerteam_timer

    fun setValues(member: Member, time: Long?) {
        textName.text = member.nickname
        textTimer.text = time?.let {
            SimpleDateFormat("m'm'ss").format(it)
        } ?: "-"
    }
}