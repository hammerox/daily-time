package com.mcustodio.dailytime.ui.daily

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mcustodio.dailytime.R
import com.mcustodio.dailytime.data.Member


/**
 * Created by mcustodio on 07/02/2018.
 */
class DailyRecyclerAdapter(var onItemClick: ((Member) -> Unit)? = null) : RecyclerView.Adapter<DailyViewHolder>() {

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



    override fun getItemCount(): Int = memberList.size

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): DailyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_daily_member, parent, false)
        return DailyViewHolder(view)
    }

    override fun onBindViewHolder(holder: DailyViewHolder, position: Int) {
        val member = memberList[position]
        val time = timeList[member.id]
        holder.setValues(member, time)
        holder.view.setOnClickListener {
            onItemClick?.invoke(member)
        }
    }

}