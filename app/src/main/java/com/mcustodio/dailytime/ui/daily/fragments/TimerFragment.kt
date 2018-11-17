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

        totalElapsedTime = DbMockViewModel.getElapsedTime()
        view.text_dailytimer_timer.setTime()
        view.text_dailytimer_milli.setMilliseconds()

        view.linear_dailytimer.setOnClickListener {
            isRunning = !isRunning

            if (isRunning) {
                view.text_dailytimer_timer.setTextColor(ContextCompat.getColor(activity!!, R.color.red_500))
                startTime = SystemClock.uptimeMillis()
                handler.postDelayed(runnable, 0)
                DbMockViewModel.isSpeaking(true)

            } else {
                view.text_dailytimer_timer.setTextColor(ContextCompat.getColor(activity!!, R.color.black))
                handler.removeCallbacks(runnable)
                DbMockViewModel.saveTime(totalElapsedTime)
                DbMockViewModel.isSpeaking(false)
            }
        }

        DbMockViewModel.selectedMember.observe(this, Observer {
            view.text_dailytimer_member.text = it?.nickname ?: ""
        })

        DbMockViewModel.selectedDaily.observe(this, Observer { daily ->
            view.text_dailytimer_notstarted.visibility = if (daily?.status == Daily.Status.NotStarted) View.VISIBLE else View.GONE
            view.linear_dailytimer.visibility = if (daily?.status == Daily.Status.Started) View.VISIBLE else View.GONE
            view.text_dailytimer_finished.visibility = if (daily?.status == Daily.Status.Finished) View.VISIBLE else View.GONE
        })

        return view
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


     private var runnable = object : Runnable {
        override fun run() {
            deltaTime = SystemClock.uptimeMillis() - startTime
            totalElapsedTime += deltaTime
            startTime = SystemClock.uptimeMillis()
            view?.text_dailytimer_timer?.setTime()
            view?.text_dailytimer_milli?.setMilliseconds()

            handler.postDelayed(this, 0)
        }
    }


}
