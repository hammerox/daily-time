package com.mcustodio.dailytime.data

import com.google.firebase.database.Exclude

data class Player(var nickname: String? = null,
                  var team_id: String? = null,
                  var time: Long? = null,
                  var is_admin: Boolean? = null,
                  var is_active: Boolean? = null,
                  @set:Exclude @get:Exclude var id: String? = null) {

}