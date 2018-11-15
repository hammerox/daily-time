package com.mcustodio.dailytime.ui.daily

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.mcustodio.dailytime.ui.addplayer.AddPlayerActivity
import com.mcustodio.dailytime.FirebaseDB
import com.mcustodio.dailytime.R
import com.mcustodio.dailytime.ui.timer.TimerActivity
import kotlinx.android.synthetic.main.activity_daily.*

class DailyActivity : AppCompatActivity() {

    companion object {
        const val dailyKey = "dailyKey"
    }

    private val adapter by lazy { DailyRecyclerAdapter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily)

        recycler_main.layoutManager = LinearLayoutManager(this)
        recycler_main.adapter = adapter

        adapter.onItemClick = {
            val intent = Intent(this, TimerActivity::class.java)
            intent.putExtra(TimerActivity.playerKey, it.id)
            startActivity(intent)
        }

        fab_main.setOnClickListener {
            val intent = Intent(this, AddPlayerActivity::class.java)
            startActivity(intent)
        }

        FirebaseDB.onPlayersChange { players ->
            adapter.playerList = players
        }
    }

}
