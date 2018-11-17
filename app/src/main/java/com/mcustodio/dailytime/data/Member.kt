package com.mcustodio.dailytime.data

import com.google.firebase.database.Exclude

data class Member(var nickname: String? = null,
                  var team_id: String? = null,
                  var admin: Boolean? = false,
                  var active: Boolean? = true,
                  var speaking: Boolean? = false,
                  @set:Exclude @get:Exclude var id: String? = null) {

}