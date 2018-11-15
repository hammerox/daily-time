package com.mcustodio.dailytime.ui.daily

import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.mcustodio.dailytime.ui.addplayer.AddPlayerActivity
import com.mcustodio.dailytime.FirebaseDB
import com.mcustodio.dailytime.R
import com.mcustodio.dailytime.ui.DbMockViewModel
import com.mcustodio.dailytime.ui.timer.TimerActivity
import kotlinx.android.synthetic.main.activity_daily.*

class DailyActivity : AppCompatActivity() {

    private val adapter by lazy { DailyRecyclerAdapter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily)

        recycler_main.layoutManager = LinearLayoutManager(this)
        recycler_main.adapter = adapter

        DbMockViewModel.players.observe(this, Observer { players ->
            adapter.playerList = players?.filter { it.is_active ?: true } ?: listOf()
        })

        DbMockViewModel.selectedDaily.observe(this, Observer { daily ->
            adapter.timeList = daily?.players_time ?: hashMapOf()
        })

        adapter.onItemClick = {
            DbMockViewModel.selectedPlayer.value = it
            val intent = Intent(this, TimerActivity::class.java)
            startActivity(intent)
        }

        fab_main.setOnClickListener {
            val intent = Intent(this, AddPlayerActivity::class.java)
            startActivity(intent)
        }
    }

}
