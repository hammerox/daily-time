package com.mcustodio.dailytime.ui.daily

import android.support.v7.widget.RecyclerView
import android.view.View
import com.mcustodio.dailytime.data.Member
import kotlinx.android.synthetic.main.item_daily_member.view.*
import java.text.SimpleDateFormat

class DailyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val textName = view.text_dailyitem_name
    private val textTimer = view.text_dailyitem_timer

    fun setValues(member: Member, time: Long?) {
        textName.text = member.nickname
        textTimer.text = time?.let {
            SimpleDateFormat("m'm'ss").format(it)
        } ?: "-"
    }
}