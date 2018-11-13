package com.mcustodio.dailytime.ui.main

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.mcustodio.dailytime.R
import com.mcustodio.dailytime.User


/**
 * Created by mcustodio on 07/02/2018.
 */
class MainRecyclerAdapter(var onItemClick: ((User) -> Unit)? = null) : RecyclerView.Adapter<MainViewHolder>() {

    var userList: List<User> = ArrayList()
        set (value) {
            field = value.sortedWith(compareBy<User> { it.time != null }
                    .thenBy { it.nickname })
            notifyDataSetChanged()
        }



    override fun getItemCount(): Int = userList.size

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MainViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_main_user, parent, false)
        return MainViewHolder(view)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val user = userList[position]
        holder.setValues(user)
        holder.view.setOnClickListener {
            onItemClick?.invoke(user)
        }
    }

}