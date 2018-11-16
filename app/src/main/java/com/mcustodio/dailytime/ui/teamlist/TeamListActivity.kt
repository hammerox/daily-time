package com.mcustodio.dailytime.ui.teamlist

import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.mcustodio.dailytime.R
import com.mcustodio.dailytime.data.Team
import com.mcustodio.dailytime.ui.DbMockViewModel
import com.mcustodio.dailytime.ui.dailylist.DailyListActivity
import com.mcustodio.dailytime.ui.timer.TimerActivity
import kotlinx.android.synthetic.main.activity_teamlist.*

class TeamListActivity : AppCompatActivity() {

    private val adapter by lazy { TeamListRecyclerAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teamlist)

        recycler_teamlist.layoutManager = LinearLayoutManager(this)
        recycler_teamlist.adapter = adapter

        DbMockViewModel.teams.observe(this, Observer {
            adapter.teamList = it ?: listOf()
        })

        adapter.onItemClick = {selectedTeam ->
            DbMockViewModel.changeTeam(selectedTeam)
            val intent = when (DbMockViewModel.selectedMember.value?.admin) {
                true -> Intent(this, DailyListActivity::class.java)
                else -> Intent(this, TimerActivity::class.java)
            }
            startActivity(intent)
        }
    }
}