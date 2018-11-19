package com.mcustodio.dailytime.data

import com.google.firebase.database.Exclude
import java.util.*
import kotlin.collections.HashMap

data class Daily(var team_id: String? = null,
                 var started_at: Long? = null,
                 var time: Long? = null,
                 var members_time: HashMap<String, Long?>? = null,
                 @set:Exclude @get:Exclude var id: String? = null) {

    enum class Status {
        NotStarted, Started, Finished
    }


    @get:Exclude val status : Status
        get() {
            return when {
                this.started_at != null && this.time == null -> Status.Started
                this.started_at != null && this.time != null -> Status.Finished
                else -> Status.NotStarted
            }
        }


    companion object {
        fun create(team: Team?) : Daily {
            return Daily().apply {
                this.team_id = team?.id
                this.started_at = Calendar.getInstance().time.time
            }
        }
    }



    fun startTime() : Date? {
        return started_at?.let {
            val cal = Calendar.getInstance()
            cal.timeInMillis = started_at!!
            cal.time
        }
    }

    fun endTime() : Date? {
        return if (started_at != null && time != null) {
            val cal = Calendar.getInstance()
            cal.timeInMillis = started_at!! + time!!
            cal.time
        } else {
            null
        }
    }
}