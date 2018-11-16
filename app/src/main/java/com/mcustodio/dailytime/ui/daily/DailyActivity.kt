package com.mcustodio.dailytime.ui.daily

import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.mcustodio.dailytime.ui.addmember.AddMemberActivity
import com.mcustodio.dailytime.R
import com.mcustodio.dailytime.ui.DbMockViewModel
import com.mcustodio.dailytime.ui.timer.TimerActivity
import kotlinx.android.synthetic.main.activity_daily.*

class DailyActivity : AppCompatActivity() {

    private val adapter by lazy { DailyRecyclerAdapter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily)

        recycler_daily.layoutManager = LinearLayoutManager(this)
        recycler_daily.adapter = adapter

        DbMockViewModel.members.observe(this, Observer { members ->
            adapter.memberList = members?.filter { it.is_active ?: true } ?: listOf()
        })

        DbMockViewModel.selectedDaily.observe(this, Observer { daily ->
            adapter.timeList = daily?.members_time ?: hashMapOf()
        })

        adapter.onItemClick = {
            DbMockViewModel.selectedMember.value = it
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }

        fab_daily.setOnClickListener {
            val intent = Intent(this, AddMemberActivity::class.java)
            startActivity(intent)
        }
    }

}
