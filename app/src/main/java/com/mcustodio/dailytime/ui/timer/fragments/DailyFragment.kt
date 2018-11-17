package com.mcustodio.dailytime.ui.timer.fragments


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
import com.google.firebase.database.FirebaseDatabase

import com.mcustodio.dailytime.R
import com.mcustodio.dailytime.ui.DbMockViewModel
import kotlinx.android.synthetic.main.fragment_timer_daily.view.*


class DailyFragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_timer_daily, container, false)

        totalElapsedTime = DbMockViewModel.getElapsedTime()
        view.text_timerdaily_timer.setTime()
        view.text_timerdaily_milli.setMilliseconds()
        view.text_timerdaily_member.text = DbMockViewModel.selectedMember.value?.nickname

        view.linear_timerdaily.setOnClickListener {
            isRunning = !isRunning

            if (isRunning) {
                view.text_timerdaily_timer.setTextColor(ContextCompat.getColor(activity!!, R.color.red_500))
                startTime = SystemClock.uptimeMillis()
                handler.postDelayed(runnable, 0)

            } else {
                view.text_timerdaily_timer.setTextColor(ContextCompat.getColor(activity!!, R.color.black))
                handler.removeCallbacks(runnable)
                saveTime(totalElapsedTime)
            }
        }

        return view
    }

    private fun saveTime(time: Long) {
        val dailyId = DbMockViewModel.selectedDaily.value?.id
        val memberId = DbMockViewModel.selectedMember.value?.id
        val reference = FirebaseDatabase.getInstance().getReference("dailies/$dailyId/members_time/")
        reference.updateChildren(hashMapOf(memberId to time as Any))
            .addOnSuccessListener { }
            .addOnFailureListener { Toast.makeText(activity!!, it.message, Toast.LENGTH_LONG).show() }
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
            view?.text_timerdaily_timer?.setTime()
            view?.text_timerdaily_milli?.setMilliseconds()

            handler.postDelayed(this, 0)
        }
    }


}
