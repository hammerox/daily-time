package com.mcustodio.dailytime

import com.google.firebase.database.*
import com.mcustodio.dailytime.data.Daily
import com.mcustodio.dailytime.data.Player

object FirebaseDB {

    val root = FirebaseDatabase.getInstance().reference

    val teamsKey = "teams"
    val teams = root.child(teamsKey)

    // todo - Remover mock team_id = -LRAIWUKQjOqTwGsJD1o
    val playersKey = "players"
    val players = root.child(playersKey).orderByChild("team_id").equalTo("-LRAIWUKQjOqTwGsJD1o").ref
    fun onPlayersChange(onChange: (List<Player>) -> Unit) {
        players.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(data: DataSnapshot) {
                val players = data.children.mapNotNull {
                    val user = it.getValue(Player::class.java)
                    user?.id = it.key
                    user
                }
                onChange(players.toList())
            }
        })
    }

    // todo - Remover mock team_id = -LRAIWUKQjOqTwGsJD1o
    val dailiesKey = "dailies"
    val dailies = root.child(dailiesKey).orderByChild("team_id").equalTo("-LRAIWUKQjOqTwGsJD1o")
    fun onDailiesChange(onChange: (List<Daily>) -> Unit) {
        dailies.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(data: DataSnapshot) {
                val dailies = data.children.mapNotNull {
                    val daily = it.getValue(Daily::class.java)
                    daily?.id = it.key
                    daily
                }
                onChange(dailies.toList())
            }
        })
    }



}