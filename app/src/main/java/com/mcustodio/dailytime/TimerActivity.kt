package com.mcustodio.dailytime

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.timer_activity.*

class TimerActivity : AppCompatActivity() {

    private var timeWhenStopped = 0L
    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.timer_activity)
        button_timer_startorpause.setOnClickListener {
            isRunning = !isRunning
            if (isRunning) {
                chronometer_timer.base = SystemClock.elapsedRealtime() - timeWhenStopped
                chronometer_timer.start()

                button_timer_startorpause.text = "PAUSE"
                button_timer_save.visibility = View.INVISIBLE

            } else {
                timeWhenStopped = SystemClock.elapsedRealtime() - chronometer_timer.base
                chronometer_timer.stop()

                button_timer_startorpause.text = "START"
                button_timer_save.visibility = View.VISIBLE
            }
         }

        button_timer_save.setOnClickListener {
            val userKey = intent.getStringExtra("userKey")
            FirebaseDB.users.child(userKey).updateChildren(hashMapOf("time" to timeWhenStopped as Any))
                .addOnSuccessListener { finish() }
                .addOnFailureListener { Toast.makeText(this, it.message, Toast.LENGTH_LONG).show() }
        }
    }

}
