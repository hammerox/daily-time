package com.mcustodio.dailytime.ui.dailylist

import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.mcustodio.dailytime.R
import com.mcustodio.dailytime.data.Daily
import kotlinx.android.synthetic.main.item_dailylist_daily.view.*
import java.text.SimpleDateFormat

class DailyListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val textName = view.text_dailylist_name
    private val textTime = view.text_dailylist_time

    private val highlightColor = ContextCompat.getColor(view.context, R.color.colorPrimary)
    private val normalColor = ContextCompat.getColor(view.context, android.R.color.tab_indicator_text)

    fun setValues(daily: Daily) {
        textName.text = daily.time_start?.let {
            SimpleDateFormat("dd MMM").format(it)
        } ?: daily.id
                ?: ""

        textTime.text = when (daily.status) {
            Daily.Status.NotStarted -> ""
            Daily.Status.Started -> "LIVE!"
            Daily.Status.Finished -> {
                val time = when {
                    daily.time_end != null && daily.time_start != null -> daily.time_end!!.time - daily.time_start!!.time
                    else -> null
                }
                time?.let {SimpleDateFormat("m'm'").format(it) } ?: ""
            }
        }

        // Colors

        textName.setTextColor(if (daily.status == Daily.Status.Started) highlightColor else normalColor)
        textTime.setTextColor(if (daily.status == Daily.Status.Started) highlightColor else normalColor)

        // TextStyle

        textName.setTypeface(textName.typeface, if (daily.status == Daily.Status.Started) Typeface.BOLD else Typeface.NORMAL)
        textTime.setTypeface(textName.typeface, if (daily.status == Daily.Status.Started) Typeface.BOLD else Typeface.NORMAL)

    }
}