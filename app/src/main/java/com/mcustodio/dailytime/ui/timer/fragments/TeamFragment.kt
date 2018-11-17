package com.mcustodio.dailytime.ui.timer.fragments


import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.mcustodio.dailytime.R
import com.mcustodio.dailytime.ui.DbMockViewModel
import com.mcustodio.dailytime.ui.timer.fragments.teamadapter.TeamRecyclerAdapter
import kotlinx.android.synthetic.main.fragment_timer_team.view.*


class TeamFragment : Fragment() {

    private val adapter by lazy { TeamRecyclerAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_timer_team, container, false)

        view.recycler_timerteam.layoutManager = LinearLayoutManager(activity)
        view.recycler_timerteam.adapter = adapter

        DbMockViewModel.members.observe(this, Observer { members ->
            adapter.memberList = members?.filter { it.team_id == DbMockViewModel.selectedTeam.value?.id ?: false }
                ?.filter { it.active ?: true }
                ?: listOf()
        })

        DbMockViewModel.selectedDaily.observe(this, Observer { daily ->
            adapter.timeList = daily?.members_time ?: hashMapOf()
        })

        adapter.onItemClick = {
            DbMockViewModel.selectedMember.value = it
        }

        return view
    }


}
