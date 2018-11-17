package com.mcustodio.dailytime.ui.timer

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.mcustodio.dailytime.R
import kotlinx.android.synthetic.main.activity_newtimer.*

class NewTimerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newtimer)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.item_newtimer_tasks -> {
                message.text = "Tarefas"
                return@OnNavigationItemSelectedListener true
            }
            R.id.item_newtimer_daily -> {
                message.text = "Daily"
                return@OnNavigationItemSelectedListener true
            }
            R.id.item_newtimer_team -> {
                message.text = "Equipe"
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}
