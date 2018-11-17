package com.mcustodio.dailytime.ui.dailylist

import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.mcustodio.dailytime.R
import com.mcustodio.dailytime.ui.DbMockViewModel
import com.mcustodio.dailytime.ui.daily.DailyActivity
import kotlinx.android.synthetic.main.activity_dailylist.*

class DailyListActivity : AppCompatActivity() {

    private val adapter by lazy { DailyListRecyclerAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dailylist)

        recycler_dailylist.layoutManager = LinearLayoutManager(this)
        recycler_dailylist.adapter = adapter

        DbMockViewModel.dailies.observe(this, Observer {
            adapter.dailyList = it ?: listOf()
        })

        adapter.onItemClick = { selectedDaily ->
            DbMockViewModel.selectedDaily.value = selectedDaily
            val intent = Intent(this, DailyActivity::class.java)
            startActivity(intent)
        }

        fab_dailylist.setOnClickListener {
            DbMockViewModel.selectedDaily.value = null
            val intent = Intent(this, DailyActivity::class.java)
            startActivity(intent)
        }
    }

}
