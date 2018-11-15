package com.mcustodio.dailytime.ui.daily

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mcustodio.dailytime.ui.addplayer.AddPlayerActivity
import com.mcustodio.dailytime.FirebaseDB
import com.mcustodio.dailytime.R
import com.mcustodio.dailytime.ui.timer.TimerActivity
import com.mcustodio.dailytime.data.Player
import kotlinx.android.synthetic.main.activity_daily.*

class DailyActivity : AppCompatActivity() {

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

        FirebaseDB.players.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(data: DataSnapshot) {
                val users = data.children.mapNotNull {
                    val user = it.getValue(Player::class.java)
                    user?.id = it.key
                    user
                }
                adapter.playerList = users.toList()
            }
        })
    }

}
