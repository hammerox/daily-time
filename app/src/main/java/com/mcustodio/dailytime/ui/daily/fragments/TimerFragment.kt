package com.mcustodio.dailytime.ui.daily.fragments


import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast

import com.mcustodio.dailytime.R
import com.mcustodio.dailytime.data.Daily
import com.mcustodio.dailytime.ui.Clock
import com.mcustodio.dailytime.ui.DailyResult
import com.mcustodio.dailytime.ui.DbMockViewModel
import kotlinx.android.synthetic.main.component_dailytimer_notstarted.view.*
import kotlinx.android.synthetic.main.component_dailytimer_started.view.*
import kotlinx.android.synthetic.main.component_dailytimer_finished.view.*
import kotlinx.android.synthetic.main.fragment_daily_timer.view.*


class TimerFragment : Fragment() {

    private val clock = Clock()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_daily_timer, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClock()
        setupClickListeners()
        setupViewModel()
    }


    private fun setupViewModel() {
        DbMockViewModel.selectedMember.observe(this, Observer {
            view?.text_dailytimer_member?.text = it?.nickname ?: ""
            setupClock()
            if (DbMockViewModel.selectedDaily.value?.status == Daily.Status.Finished) calculateResult()
        })

        DbMockViewModel.selectedDaily.observe(this, Observer { daily ->
            view?.component_dailytimer_notstarted?.visibility = if (daily?.let { it.status == Daily.Status.NotStarted } ?: true) View.VISIBLE else View.GONE
            view?.component_dailytimer_started?.visibility = if (daily?.status == Daily.Status.Started) View.VISIBLE else View.GONE
            view?.component_dailytimer_finished?.visibility = if (daily?.status == Daily.Status.Finished) View.VISIBLE else View.GONE

            // Layout visibility
            view?.text_dailytimer_notstarted?.visibility = if (DbMockViewModel.isAdmin.value != true) View.VISIBLE else View.GONE
            view?.buttonframe_dailytimer_startdaily?.visibility = if (DbMockViewModel.isAdmin.value == true) View.VISIBLE else View.GONE
            view?.buttonframe_dailytimer_closedaily?.visibility = if (DbMockViewModel.isAdmin.value == true && daily?.status == Daily.Status.Started) View.VISIBLE else View.GONE

            if (daily?.status == Daily.Status.Finished) calculateResult()
        })
    }


    private fun setupClickListeners() {
        view?.linear_dailytimer_clock?.setOnClickListener {
            if (!clock.isRunning) {
                view?.text_dailytimer_timer?.setTextColor(ContextCompat.getColor(activity!!, R.color.red_500))
                clock.start()
                DbMockViewModel.isSpeaking(true)

            } else {
                view?.text_dailytimer_timer?.setTextColor(ContextCompat.getColor(activity!!, R.color.black))
                clock.stop()
                DbMockViewModel.saveMemberTime(clock.elapsedTime)
                DbMockViewModel.isSpeaking(false)
            }
        }

        view?.button_dailytimer_startdaily?.setOnClickListener {
            DbMockViewModel.createDaily()
                .addOnSuccessListener {}
                .addOnFailureListener { Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show() }
        }

        view?.button_dailytimer_closedaily?.setOnClickListener {
            DbMockViewModel.closeDaily()
                .addOnSuccessListener {}
                .addOnFailureListener { Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show() }
        }
    }


    private fun setupClock() {
        val initialTime = DbMockViewModel.getElapsedTime()
        clock.setup(initialTime)
        clock.onTick = { time ->
            view?.text_dailytimer_timer?.setTime(time)
            view?.text_dailytimer_milli?.setMilliseconds(time)
        }

        view?.text_dailytimer_timer?.setTime(initialTime)
        view?.text_dailytimer_milli?.setMilliseconds(initialTime)
    }


    private fun TextView.setTime(time: Long?, signed: Boolean = false) {
        this.text = if (time != null) {
            val totalSeconds = (time / 1000).toInt()
            val minutes = Math.abs(totalSeconds / 60)
            val seconds = Math.abs(totalSeconds % 60)
            val timeString = "$minutes:${String.format("%02d", seconds)}"
            if (signed) (if (time > 0) "+$timeString" else "-$timeString")
            else timeString
        } else {
            "N/A"
        }

    }


    private fun TextView.setMilliseconds(time: Long) {
        val milliSeconds = (time % 1000).toInt()

        this.text = (String.format("%03d", milliSeconds))
    }


    private fun calculateResult() {
        val result = DailyResult(DbMockViewModel.selectedDaily.value!!, DbMockViewModel.dailies.value!!, DbMockViewModel.selectedMember.value!!)

        val positiveColor = ContextCompat.getColor(view?.context!!, R.color.colorPrimary)
        val negativeColor = ContextCompat.getColor(view?.context!!, R.color.red_500)

        view?.text_dailytimer_membertime?.setTime(result.member.currentTime)
        view?.text_dailytimer_memberalltime?.setTime(result.member.allTime)
        view?.text_dailytimer_memberweektime?.setTime(result.member.weekTime)
        view?.text_dailytimer_memberlasttime?.setTime(result.member.lastTime)
        view?.text_dailytimer_memberalldiff?.setTime(result.member.allDiff, true)
        view?.text_dailytimer_memberweekdiff?.setTime(result.member.weekDiff, true)
        view?.text_dailytimer_memberlastdiff?.setTime(result.member.lastDiff, true)

        view?.text_dailytimer_memberalldiff?.setTextColor(if (result.member.allDiff ?: 0 > 0) negativeColor else positiveColor)
        view?.text_dailytimer_memberweekdiff?.setTextColor(if (result.member.weekDiff ?: 0 > 0) negativeColor else positiveColor)
        view?.text_dailytimer_memberlastdiff?.setTextColor(if (result.member.lastDiff ?: 0 > 0) negativeColor else positiveColor)

//        view?.text_dailytimer_teamtime?.text
//        view?.text_dailytimer_teamalltime?.text
//        view?.text_dailytimer_teamweektime?.text
//        view?.text_dailytimer_teamlasttime?.text
//        view?.text_dailytimer_teamalldiff?.text
//        view?.text_dailytimer_teamweekdiff?.text
//        view?.text_dailytimer_teamlastdiff?.text
    }


}
