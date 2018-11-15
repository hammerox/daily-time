package com.mcustodio.dailytime.ui.dailylist

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.mcustodio.dailytime.FirebaseDB
import com.mcustodio.dailytime.R
import com.mcustodio.dailytime.ui.daily.DailyActivity
import kotlinx.android.synthetic.main.activity_dailylist.*

class DailyListActivity : AppCompatActivity() {

    private val adapter by lazy { DailyListRecyclerAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dailylist)

        recycler_dailylist.layoutManager = LinearLayoutManager(this)
        recycler_dailylist.adapter = adapter

        adapter.onItemClick = {
            val intent = Intent(this, DailyActivity::class.java)
            intent.putExtra(DailyActivity.dailyKey, it.id)
            startActivity(intent)
        }

        FirebaseDB.onDailiesChange { dailies ->
            adapter.dailyList = dailies
        }
    }
}
