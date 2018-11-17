package com.mcustodio.dailytime.ui.daily.fragments.teamadapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mcustodio.dailytime.R
import com.mcustodio.dailytime.data.Member

class TeamRecyclerAdapter(var onItemClick: ((Member) -> Unit)? = null) : RecyclerView.Adapter<TeamViewHolder>() {

    var memberList: List<Member> = ArrayList()
        set (value) {
            field = value.sortedWith(compareBy { it.nickname })
            notifyDataSetChanged()
        }

    var timeList: Map<String, Long?> = HashMap()
        set (value) {
            field = value
            notifyDataSetChanged()
        }

    var selectedMember: Member? = null
        set (value) {
            field = value
            notifyDataSetChanged()
        }



    override fun getItemCount(): Int = memberList.size

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): TeamViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_timerteam_member, parent, false)
        return TeamViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        val member = memberList[position]
        val time = timeList[member.id]
        val isSelectedMember = member.id == selectedMember?.id
        holder.setValues(member, time, isSelectedMember)
        holder.view.setOnClickListener {
            onItemClick?.invoke(member)
        }
    }

}