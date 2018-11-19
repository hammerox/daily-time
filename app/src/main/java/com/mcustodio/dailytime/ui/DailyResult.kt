package com.mcustodio.dailytime.ui

import com.mcustodio.dailytime.data.Daily
import com.mcustodio.dailytime.data.Member

class DailyResult(currentDaily: Daily, dailies: List<Daily>, member: Member) {

    val member: Result

    init {
        val allDailies = dailies
            .filter { it.status == Daily.Status.Finished }
            .filter { it.time_start?.time ?: 0 < currentDaily.time_start?.time ?: 0 }    // Ignora o tempo atual
            .sortedByDescending { it.time_start }  // Ordena do mais recente

        val lastWeek = allDailies.filterIndexed { i, _ -> i < 5 }
        val lastDaily = allDailies.firstOrNull()

        this.member = Result(
            currentTime = currentDaily.members_time?.get(member.id),
            allTime = allDailies.mapNotNull { it.members_time?.get(member.id) }.sum(),
            weekTime = lastWeek.mapNotNull { it.members_time?.get(member.id) }.sum(),
            lastTime = lastDaily?.members_time?.get(member.id)
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
    ) {}



}