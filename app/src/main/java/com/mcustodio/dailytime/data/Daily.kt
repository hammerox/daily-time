package com.mcustodio.dailytime.data

import com.google.firebase.database.Exclude
import java.util.*
import kotlin.collections.HashMap

data class Daily(var team_id: String? = null,
                 var time_start: Date? = null,
                 var time_end: Date? = null,
                 var players_time: HashMap<String, Long?>? = null,
                 @set:Exclude @get:Exclude var id: String? = null) {
}