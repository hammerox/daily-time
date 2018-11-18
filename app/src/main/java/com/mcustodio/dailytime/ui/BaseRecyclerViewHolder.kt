package com.mcustodio.dailytime.ui

import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.mcustodio.dailytime.R

abstract class BaseRecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val highlightColor = ContextCompat.getColor(view.context, R.color.colorPrimary)
    val normalColor = ContextCompat.getColor(view.context, android.R.color.tab_indicator_text)

    val montserratRegular = ResourcesCompat.getFont(view.context, R.font.montserrat_regular)
    val montserratBold = ResourcesCompat.getFont(view.context, R.font.montserrat_bold)
}