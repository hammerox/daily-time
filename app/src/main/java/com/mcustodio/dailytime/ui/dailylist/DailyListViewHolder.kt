package com.mcustodio.dailytime.ui.dailylist

import android.support.v7.widget.RecyclerView
import android.view.View
import com.mcustodio.dailytime.data.Daily
import kotlinx.android.synthetic.main.item_dailylist_daily.view.*

class DailyListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val textName = view.text_dailylist

    fun setValues(daily: Daily) {
        textName.text = daily.id
    }
}