package com.mcustodio.dailytime.ui.daily.fragments


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.mcustodio.dailytime.R
import com.mcustodio.dailytime.ui.DbMockViewModel
import com.mcustodio.dailytime.ui.daily.fragments.teamadapter.TeamRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_daily_team.view.*


class TeamFragment : Fragment() {

    private val adapter by lazy { TeamRecyclerAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_daily_team, container, false)

        view.recycler_dailyteam.layoutManager = LinearLayoutManager(activity)
        view.recycler_dailyteam.adapter = adapter

        DbMockViewModel.members.observe(this, Observer { members ->
            adapter.memberList = members?.filter { it.team_id == DbMockViewModel.selectedTeam.value?.id ?: false }
                ?.filter { it.active ?: true }
                ?: listOf()
        })

        DbMockViewModel.selectedMember.observe(this, Observer {
            adapter.selectedMember = it
        })

        DbMockViewModel.selectedDaily.observe(this, Observer { daily ->
            adapter.timeList = daily?.members_time ?: hashMapOf()
        })

        adapter.onItemClick = {
            if (DbMockViewModel.isAdmin.value == true) {
                DbMockViewModel.selectedMember.value = it
                Toast.makeText(activity, "Empersonado ${it.nickname}", Toast.LENGTH_LONG).show()
            }
        }

        return view
    }


}
