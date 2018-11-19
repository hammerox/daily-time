package com.mcustodio.dailytime.data

import com.google.firebase.database.Exclude
import java.util.*
import kotlin.collections.HashMap

data class Daily(var team_id: String? = null,
                 var time_start: Date? = null,
                 var time_end: Date? = null,
                 var members_time: HashMap<String, Long?>? = null,
                 @set:Exclude @get:Exclude var id: String? = null) {

    enum class Status {
        NotStarted, Started, Finished
    }


    val status : Status
        get() {
            return when {
                this.time_start != null && this.time_end == null -> Status.Started
                this.time_start != null && this.time_end != null -> Status.Finished
                this.time_start == null && this.time_end == null -> Status.Finished
                else -> Status.NotStarted
            }
        }


    companion object {
        fun create(team: Team?) : Daily {
            return Daily().apply {
                this.team_id = team?.id
                this.time_start = Calendar.getInstance().time
            }
        }
    }


    fun timeElapsed() : Long? {
        return if (time_start != null && time_end != null) {
            time_end!!.time - time_start!!.time
        } else {
            null
        }
    }
}