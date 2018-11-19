package com.mcustodio.dailytime.ui

import com.mcustodio.dailytime.data.Daily
import com.mcustodio.dailytime.data.Member

class DailyResult(currentDaily: Daily, dailies: List<Daily>, member: Member) {

    val member: Result
    val team: Result

    init {
        val allDailies = dailies
            .filter { it.status == Daily.Status.Finished }
            .filter { it.started_at ?: 0 < currentDaily.started_at ?: 0 }    // Ignora o tempo atual
            .sortedByDescending { it.started_at }  // Ordena do mais recente

        val memberDailies = allDailies.filter { it.members_time?.containsKey(member.id) ?: false }.takeIf { it.isNotEmpty() }
        val memberLastWeek = memberDailies?.filterIndexed { i, _ -> i < 5 }.takeIf { it?.isNotEmpty() == true }
        val memberLastDaily = memberDailies?.firstOrNull()

        this.member = Result(
            currentTime = currentDaily.members_time?.get(member.id),
            allTime = memberDailies
                ?.mapNotNull { it.members_time?.get(member.id) }
                ?.sum()
                ?.div(memberDailies.size),
            weekTime = memberLastWeek
                ?.mapNotNull { it.members_time?.get(member.id) }
                ?.sum()
                ?.div(memberLastWeek.size),
            lastTime = memberLastDaily?.members_time?.get(member.id)
        )


        val teamDailies = allDailies.takeIf { it.isNotEmpty() }
        val teamLastWeek = teamDailies?.filterIndexed { i, _ -> i < 5 }.takeIf { it?.isNotEmpty() == true }
        val teamLastDaily = teamDailies?.firstOrNull()

        this.team = Result(
            currentTime = currentDaily.time,
            allTime = teamDailies?.mapNotNull { it.time }?.sum()?.div(teamDailies.size),
            weekTime = teamLastWeek?.mapNotNull { it.time }?.sum()?.div(teamLastWeek.size),
            lastTime = teamLastDaily?.time
        )
    }


    data class Result (
        val currentTime: Long? = null,

        val allTime: Long? = null,
        val weekTime: Long? = null,
        val lastTime: Long? = null,

        val allDiff: Long? = allTime?.let { currentTime?.minus(allTime) },
        val weekDiff: Long? = weekTime?.let { currentTime?.minus(weekTime) },
        val lastDiff: Long? = lastTime?.let { currentTime?.minus(lastTime)}
    )

}