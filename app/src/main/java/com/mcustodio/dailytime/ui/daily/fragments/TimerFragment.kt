package com.mcustodio.dailytime.ui.daily.fragments


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import com.mcustodio.dailytime.R
import com.mcustodio.dailytime.data.Daily
import com.mcustodio.dailytime.ui.DbMockViewModel
import kotlinx.android.synthetic.main.fragment_daily_timer.view.*


class TimerFragment : Fragment() {

    private var isRunning = false
    private val handler = Handler()
    private var totalElapsedTime:Long = 0
    private var startTime:Long = 0
    private var deltaTime: Long = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_daily_timer, container, false)

        setupClock(view)
        setupClickListeners(view)
        setupViewModel(view)

        return view
    }

    private fun setupViewModel(view: View) {
        DbMockViewModel.selectedMember.observe(this, Observer {
            view.text_dailytimer_member.text = it?.nickname ?: ""
            setupClock(view)
        })

        DbMockViewModel.selectedDaily.observe(this, Observer { daily ->
            // Layout visibility
            view.text_dailytimer_notstarted.visibility = if (DbMockViewModel.isAdmin.value != true && daily?.status == Daily.Status.NotStarted) View.VISIBLE else View.GONE
            view.frame_dailytimer_startdaily.visibility = if (DbMockViewModel.isAdmin.value == true && (daily == null || daily.status == Daily.Status.NotStarted)) View.VISIBLE else View.GONE
            view.frame_dailytimer_closedaily.visibility = if (DbMockViewModel.isAdmin.value == true && daily?.status == Daily.Status.Started) View.VISIBLE else View.GONE
            view.linear_dailytimer_clock.visibility = if (daily?.status == Daily.Status.Started) View.VISIBLE else View.GONE
            view.text_dailytimer_finished.visibility = if (daily?.status == Daily.Status.Finished) View.VISIBLE else View.GONE
        })
    }

    private fun setupClickListeners(view: View) {
        view.linear_dailytimer_clock.setOnClickListener {
            isRunning = !isRunning

            if (isRunning) {
                view.text_dailytimer_timer.setTextColor(ContextCompat.getColor(activity!!, R.color.red_500))
                startTime = SystemClock.uptimeMillis()
                handler.postDelayed(onClockTick, 0)
                DbMockViewModel.isSpeaking(true)

            } else {
                view.text_dailytimer_timer.setTextColor(ContextCompat.getColor(activity!!, R.color.black))
                handler.removeCallbacks(onClockTick)
                DbMockViewModel.saveMemberTime(totalElapsedTime)
                DbMockViewModel.isSpeaking(false)
            }
        }

        view.button_dailytimer_startdaily.setOnClickListener {
            DbMockViewModel.createDaily()
                .addOnSuccessListener {}
                .addOnFailureListener { Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show() }
        }

        view.button_dailytimer_closedaily.setOnClickListener {
            DbMockViewModel.closeDaily()
                .addOnSuccessListener {}
                .addOnFailureListener { Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show() }
        }
    }

    private fun setupClock(view: View) {
        handler.removeCallbacks(onClockTick)
        totalElapsedTime = DbMockViewModel.getElapsedTime()
        view.text_dailytimer_timer.setTime()
        view.text_dailytimer_milli.setMilliseconds()
    }


     private var onClockTick = object : Runnable {
        override fun run() {
            deltaTime = SystemClock.uptimeMillis() - startTime
            totalElapsedTime += deltaTime
            startTime = SystemClock.uptimeMillis()
            view?.text_dailytimer_timer?.setTime()
            view?.text_dailytimer_milli?.setMilliseconds()

            handler.postDelayed(this, 0)
        }
    }

    private fun TextView.setTime() {
        val totalSeconds = (totalElapsedTime / 1000).toInt()
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60

        this.text = ("" + minutes + ":" + String.format("%02d", seconds))
    }

    private fun TextView.setMilliseconds() {
        val milliSeconds = (totalElapsedTime % 1000).toInt()

        this.text = (String.format("%03d", milliSeconds))
    }


}
