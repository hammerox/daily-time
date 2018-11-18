package com.mcustodio.dailytime.ui.daily.fragments.teamadapter

import android.view.View
import com.mcustodio.dailytime.data.Member
import com.mcustodio.dailytime.ui.BaseRecyclerViewHolder
import kotlinx.android.synthetic.main.item_timerteam_member.view.*
import java.text.SimpleDateFormat

class TeamViewHolder(val view: View) : BaseRecyclerViewHolder(view) {

    private val textName = view.text_dailyteam_name
    private val textTimer = view.text_dailyteam_timer
    private val speakingImage = view.image_dailyitem_speaking


    fun setValues(member: Member, time: Long?, isSelectedMember: Boolean) {
        textName.text = member.nickname

        textTimer.text = time?.let {
            if (time > 0) SimpleDateFormat("m'm'ss").format(it)
            else ""
        } ?: ""

        // Colors

        val color = if (isSelectedMember) highlightColor else normalColor
        textName.setTextColor(color)
        textTimer.setTextColor(color)

        // TextStyle

        val style = if (isSelectedMember) montserratBold else montserratRegular
        textName.typeface = style
        textTimer.typeface = style

        // Visibilities

        textTimer.visibility = if (member.speaking == true) View.GONE else View.VISIBLE
        speakingImage.visibility = if (member.speaking == true) View.VISIBLE else View.GONE
    }
}