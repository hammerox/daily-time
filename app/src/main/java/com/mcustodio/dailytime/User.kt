package com.mcustodio.dailytime

import com.google.firebase.database.Exclude

data class User(var nickname: String? = null,
                var team_id: String? = null,
                var time: Long? = null,
                @set:Exclude @get:Exclude var id: String? = null) {

}