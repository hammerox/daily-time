package com.mcustodio.dailytime.ui.main

import android.support.v7.widget.RecyclerView
import android.view.View
import com.mcustodio.dailytime.User
import kotlinx.android.synthetic.main.item_main_user.view.*
import java.text.SimpleDateFormat

class MainViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val textName = view.text_mainitem_name
    private val textTimer = view.text_mainitem_timer

    fun setValues(user: User) {
        textName.text = user.nickname
        textTimer.text = user.time?.let {
            SimpleDateFormat("m'm'ss").format(it)
        } ?: "-"
    }
}