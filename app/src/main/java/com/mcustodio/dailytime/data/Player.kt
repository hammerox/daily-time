package com.mcustodio.dailytime.data

import com.google.firebase.database.Exclude

data class Player(var nickname: String? = null,
                  var team_id: String? = null,
                  var is_admin: Boolean? = false,
                  var is_active: Boolean? = true,
                  @set:Exclude @get:Exclude var id: String? = null) {

}