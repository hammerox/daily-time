package com.mcustodio.dailytime.ui.teamlist

import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.mcustodio.dailytime.R
import com.mcustodio.dailytime.data.Member
import com.mcustodio.dailytime.data.Team
import com.mcustodio.dailytime.ui.DbMockViewModel
import com.mcustodio.dailytime.ui.dailylist.DailyListActivity
import com.mcustodio.dailytime.ui.timer.NewTimerActivity
import com.mcustodio.dailytime.ui.timer.TimerActivity
import kotlinx.android.synthetic.main.activity_teamlist.*

class TeamListActivity : AppCompatActivity() {

    private val adapter by lazy { TeamListRecyclerAdapter() }
    private var tryToAutoOpen = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teamlist)

        recycler_teamlist.layoutManager = LinearLayoutManager(this)
        recycler_teamlist.adapter = adapter

        DbMockViewModel.teams.observe(this, Observer {
            adapter.teamList = it ?: listOf()
            autoSelectIfPossible(it, DbMockViewModel.members.value)
        })

        DbMockViewModel.members.observe(this, Observer {
            autoSelectIfPossible(DbMockViewModel.teams.value, it)
        })

        adapter.onItemClick = { selectedTeam ->
            selectTeam(selectedTeam)
        }
    }

    override fun onPause() {
        super.onPause()
        tryToAutoOpen = false   // Desiste de tentar auto-abrir assim que uma nova tela aparece
    }

    private fun autoSelectIfPossible(teamList: List<Team>?, memberList: List<Member>?) {
        // Abre único time caso só tenha um
        if (tryToAutoOpen && teamList?.size == 1 && memberList?.isNotEmpty() == true) {
            tryToAutoOpen = false
            selectTeam(teamList[0])
        }
    }

    private fun selectTeam(selectedTeam: Team) {
        DbMockViewModel.changeTeam(selectedTeam)
        val intent = when (DbMockViewModel.selectedMember.value?.admin) {
            true -> Intent(this, DailyListActivity::class.java)
            else -> Intent(this, NewTimerActivity::class.java)
        }
        startActivity(intent)
    }
}