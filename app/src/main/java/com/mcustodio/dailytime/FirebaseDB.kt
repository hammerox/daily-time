package com.mcustodio.dailytime

import com.google.firebase.database.*
import com.mcustodio.dailytime.data.Player

object FirebaseDB {

    val root = FirebaseDatabase.getInstance().reference

    val teamsKey = "teams"
    val teams = root.child(teamsKey)

    val playersKey = "players"
    val players = root.child(playersKey)
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

    val dailiesKey = "dailies"
    val dailies = root.child(dailiesKey)



}