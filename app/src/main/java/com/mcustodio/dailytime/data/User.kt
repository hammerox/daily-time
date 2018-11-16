package com.mcustodio.dailytime.data

import com.google.firebase.database.Exclude

data class User(var email : String? = null,
                var saved_teams_to_member : Map<String, String?> = hashMapOf(),
                @set:Exclude @get:Exclude var id: String? = null) {

}