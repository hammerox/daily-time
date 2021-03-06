package com.mcustodio.dailytime.data

import com.google.firebase.database.Exclude

data class Team(var name: String? = null,
                var code: String? = null,
                @set:Exclude @get:Exclude var id: String? = null) {
}