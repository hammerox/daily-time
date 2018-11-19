package com.mcustodio.dailytime.ui.dailylist

import android.view.View
import com.mcustodio.dailytime.data.Daily
import com.mcustodio.dailytime.ui.BaseRecyclerViewHolder
import kotlinx.android.synthetic.main.item_dailylist_daily.view.*
import java.text.SimpleDateFormat
import java.util.*

class DailyListViewHolder(val view: View) : BaseRecyclerViewHolder(view) {

    private val textName = view.text_dailylist_name
    private val textTime = view.text_dailylist_time


    fun setValues(daily: Daily) {
        textName.text = daily.startTime()?.let {
            SimpleDateFormat("dd MMM").format(it)
        } ?: daily.id
                ?: ""

        textTime.text = when (daily.status) {
            Daily.Status.NotStarted -> ""
            Daily.Status.Started -> "LIVE!"
            Daily.Status.Finished -> { daily.time?.let { SimpleDateFormat("m'm'").format(it) } ?: "" }
        }

        // Colors

        val color = if (daily.status == Daily.Status.Started) highlightColor else normalColor
        textName.setTextColor(color)
        textTime.setTextColor(color)

        // TextStyle

        val style = if (daily.status == Daily.Status.Started) montserratBold else montserratRegular
        textName.typeface = style
        textTime.typeface = style

    }
}