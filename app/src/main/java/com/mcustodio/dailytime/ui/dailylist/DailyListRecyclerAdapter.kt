package com.mcustodio.dailytime.ui.dailylist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mcustodio.dailytime.R
import com.mcustodio.dailytime.data.Daily


/**
 * Created by mcustodio on 07/02/2018.
 */
class DailyListRecyclerAdapter(var onItemClick: ((Daily) -> Unit)? = null) : RecyclerView.Adapter<DailyListViewHolder>() {

    var dailyList: List<Daily> = ArrayList()
        set (value) {
            field = value.sortedWith(compareByDescending { it.time_start })
            notifyDataSetChanged()
        }



    override fun getItemCount(): Int = dailyList.size

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): DailyListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_dailylistitem_daily, parent, false)
        return DailyListViewHolder(view)
    }

    override fun onBindViewHolder(holder: DailyListViewHolder, position: Int) {
        val daily = dailyList[position]
        holder.setValues(daily)
        holder.view.setOnClickListener {
            onItemClick?.invoke(daily)
        }
    }

}