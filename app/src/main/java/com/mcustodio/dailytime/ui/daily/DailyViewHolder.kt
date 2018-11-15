package com.mcustodio.dailytime.ui.daily

import android.support.v7.widget.RecyclerView
import android.view.View
import com.mcustodio.dailytime.data.Player
import kotlinx.android.synthetic.main.item_daily_player.view.*
import java.text.SimpleDateFormat

class DailyViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val textName = view.text_dailyitem_name
    private val textTimer = view.text_dailyitem_timer

    fun setValues(player: Player, time: Long?) {
        textName.text = player.nickname
        textTimer.text = time?.let {
            SimpleDateFormat("m'm'ss").format(it)
        } ?: "-"
    }
}