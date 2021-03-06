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
import com.google.firebase.functions.FirebaseFunctions
import com.mcustodio.dailytime.BuildConfig

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

    private val goodColor by lazy { ContextCompat.getColor(view?.context!!, R.color.colorPrimary) }
    private val badColor by lazy { ContextCompat.getColor(view?.context!!, R.color.red_500) }
    private val neutralColor by lazy { ContextCompat.getColor(view?.context!!, android.R.color.tab_indicator_text) }


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
            view?.text_dailytimer_membername?.text = it?.nickname ?: ""
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
                view?.text_dailytimer_timer?.setTextColor(ContextCompat.getColor(activity!!, R.color.colorPrimary))
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
                .addOnSuccessListener {
                    if (!BuildConfig.DEBUG) {
                        val data = hashMapOf("team_id" to DbMockViewModel.selectedTeam.value?.id)
                        FirebaseFunctions.getInstance().getHttpsCallable("notifyDailyStarted").call(data)
                    }
                }
                .addOnFailureListener { Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show() }
        }

        view?.button_dailytimer_closedaily?.setOnClickListener {
            DbMockViewModel.closeDaily()
                .addOnSuccessListener {}
                .addOnFailureListener { Toast.makeText(activity, it.message, Toast.LENGTH_LONG).show() }
        }

        view?.linear_dailytimer_randommember?.setOnClickListener {
            view?.text_dailytimer_randommember?.text = DbMockViewModel.chooseRandomMemberToSpeak()?.nickname ?: "-"
        }
    }


    private fun setupClock() {
        val initialTime = DbMockViewModel.getElapsedTime()
        clock.setup(initialTime)
        clock.onTick = { time ->
            view?.text_dailytimer_timer?.setTime(time)
            view?.text_dailytimer_milli?.setMilliseconds(time)
        }

        view?.text_dailytimer_timer?.setTime(initialTime, zerosIfNull = true)
        view?.text_dailytimer_milli?.setMilliseconds(initialTime)
    }


    private fun TextView.setTime(time: Long?, signed: Boolean = false, zerosIfNull: Boolean = false) {
        this.text = if (time != null && time != 0L) {
            val totalSeconds = (time / 1000).toInt()
            val minutes = Math.abs(totalSeconds / 60)
            val seconds = Math.abs(totalSeconds % 60)
            val timeString = "$minutes:${String.format("%02d", seconds)}"
            if (signed) (if (time > 0) "+$timeString" else "-$timeString")
            else timeString
        } else {
            if (zerosIfNull) "0:00" else "-"
        }

    }


    private fun TextView.setMilliseconds(time: Long) {
        val milliSeconds = (time % 1000).toInt()

        this.text = (String.format("%03d", milliSeconds))
    }


    private fun calculateResult() {
        val result = DailyResult(DbMockViewModel.selectedDaily.value!!, DbMockViewModel.dailies.value!!, DbMockViewModel.selectedMember.value!!)

        view?.text_dailytimer_membertime?.setTime(result.member.currentTime)
        view?.text_dailytimer_memberalltime?.setTime(result.member.allTime)
        view?.text_dailytimer_memberweektime?.setTime(result.member.weekTime)
        view?.text_dailytimer_memberlasttime?.setTime(result.member.lastTime)
        view?.text_dailytimer_memberalldiff?.setTime(result.member.allDiff, true)
        view?.text_dailytimer_memberweekdiff?.setTime(result.member.weekDiff, true)
        view?.text_dailytimer_memberlastdiff?.setTime(result.member.lastDiff, true)

        view?.text_dailytimer_memberalltime?.setTextColor(result.member.allDiff.color())
        view?.text_dailytimer_memberweektime?.setTextColor(result.member.weekDiff.color())
        view?.text_dailytimer_memberlasttime?.setTextColor(result.member.lastDiff.color())
        view?.text_dailytimer_memberalldiff?.setTextColor(result.member.allDiff.color())
        view?.text_dailytimer_memberweekdiff?.setTextColor(result.member.weekDiff.color())
        view?.text_dailytimer_memberlastdiff?.setTextColor(result.member.lastDiff.color())


        view?.text_dailytimer_teamtime?.setTime(result.team.currentTime)
        view?.text_dailytimer_teamalltime?.setTime(result.team.allTime)
        view?.text_dailytimer_teamweektime?.setTime(result.team.weekTime)
        view?.text_dailytimer_teamlasttime?.setTime(result.team.lastTime)
        view?.text_dailytimer_teamalldiff?.setTime(result.team.allDiff, true)
        view?.text_dailytimer_teamweekdiff?.setTime(result.team.weekDiff, true)
        view?.text_dailytimer_teamlastdiff?.setTime(result.team.lastDiff, true)

        view?.text_dailytimer_teamalltime?.setTextColor(result.team.allDiff.color())
        view?.text_dailytimer_teamweektime?.setTextColor(result.team.weekDiff.color())
        view?.text_dailytimer_teamlasttime?.setTextColor(result.team.lastDiff.color())
        view?.text_dailytimer_teamalldiff?.setTextColor(result.team.allDiff.color())
        view?.text_dailytimer_teamweekdiff?.setTextColor(result.team.weekDiff.color())
        view?.text_dailytimer_teamlastdiff?.setTextColor(result.team.lastDiff.color())
    }


    fun Long?.color() : Int {
        return when  {
            this == null -> neutralColor
            this > 0 -> badColor
            this < 0 -> goodColor
            else -> neutralColor
        }
    }


}
