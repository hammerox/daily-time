package com.mcustodio.dailytime.ui.daily.fragments.teamadapter

import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import com.mcustodio.dailytime.R
import com.mcustodio.dailytime.data.Member
import kotlinx.android.synthetic.main.item_timerteam_member.view.*
import java.text.SimpleDateFormat

class TeamViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    private val textName = view.text_dailyteam_name
    private val textTimer = view.text_dailyteam_timer
    private val speakingImage = view.image_dailyitem_speaking

    private val highlightColor = ContextCompat.getColor(view.context, R.color.colorPrimary)
    private val normalColor = ContextCompat.getColor(view.context, android.R.color.tab_indicator_text)

    fun setValues(member: Member, time: Long?, isSelectedMember: Boolean) {
        textName.text = member.nickname

        textTimer.text = time?.let {
            if (time > 0) SimpleDateFormat("m'm'ss").format(it)
            else ""
        } ?: ""

        // Colors

        textName.setTextColor(if (isSelectedMember) highlightColor else normalColor)
        textTimer.setTextColor(if (isSelectedMember) highlightColor else normalColor)

        // TextStyle

        textName.setTypeface(textName.typeface, if (isSelectedMember) Typeface.BOLD else Typeface.NORMAL)
        textTimer.setTypeface(textName.typeface, if (isSelectedMember) Typeface.BOLD else Typeface.NORMAL)

        // Visibilities

        textTimer.visibility = if (member.speaking == true) View.GONE else View.VISIBLE
        speakingImage.visibility = if (member.speaking == true) View.VISIBLE else View.GONE
    }
}