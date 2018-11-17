package com.mcustodio.dailytime.ui.timer

import android.arch.lifecycle.Observer
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.mcustodio.dailytime.Preferences
import com.mcustodio.dailytime.R
import com.mcustodio.dailytime.ui.DbMockViewModel
import kotlinx.android.synthetic.main.activity_timer.*

class TimerActivity : AppCompatActivity() {

    private val preferences by lazy { Preferences(this) }
    private var timeWhenStopped = 0L
    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        edit_timer_whatyoudid.setText(preferences.whatYouDid)
        edit_timer_whatyouwilldo.setText(preferences.whatYouWillDo)
        edit_timer_difficulties.setText(preferences.difficulties)

        DbMockViewModel.selectedDaily.observe(this, Observer {
            // todo - Esconder layout caso activeDaily == null
            // todo - Adicionar botão comunitário para iniciar a daily
            // todo - Adicionar botão comunitário para encerrar daily
        })

        timeWhenStopped = DbMockViewModel.getElapsedTime()
        chronometer_timer.base = SystemClock.elapsedRealtime() - timeWhenStopped

        button_timer_startorpause.setOnClickListener {
            isRunning = !isRunning
            if (isRunning) {
                chronometer_timer.base = SystemClock.elapsedRealtime() - timeWhenStopped
                chronometer_timer.start()
                chronometer_timer.setTextColor(ContextCompat.getColor(this, R.color.red_500))

                button_timer_startorpause.text = "PAUSE"
                button_timer_save.isEnabled = false

            } else {
                timeWhenStopped = SystemClock.elapsedRealtime() - chronometer_timer.base
                chronometer_timer.stop()
                chronometer_timer.setTextColor(ContextCompat.getColor(this, R.color.black))

                button_timer_startorpause.text = "START"
                button_timer_save.isEnabled = true
            }
        }

        button_timer_save.setOnClickListener {
            val dailyId = DbMockViewModel.selectedDaily.value?.id
            val memberId = DbMockViewModel.selectedMember.value?.id
            val reference = FirebaseDatabase.getInstance().getReference("dailies/$dailyId/members_time/")
            reference.updateChildren(hashMapOf(memberId to timeWhenStopped as Any))
                .addOnSuccessListener { finish() }
                .addOnFailureListener { Toast.makeText(this, it.message, Toast.LENGTH_LONG).show() }
        }
    }


    override fun onPause() {
        super.onPause()
        preferences.whatYouDid = edit_timer_whatyoudid.text.toString()
        preferences.whatYouWillDo = edit_timer_whatyouwilldo.text.toString()
        preferences.difficulties = edit_timer_difficulties.text.toString()
    }



}
