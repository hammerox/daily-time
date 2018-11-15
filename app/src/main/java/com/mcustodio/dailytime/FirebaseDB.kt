package com.mcustodio.dailytime

import com.google.firebase.database.FirebaseDatabase

object FirebaseDB {

    // Write a message to the database
    val root = FirebaseDatabase.getInstance().reference

    val teamsKey = "teams"
    val teams = root.child(teamsKey)

    val playersKey = "players"
    val players = root.child(playersKey)

    val dailiesKey = "dailies"
    val dailies = root.child(dailiesKey)



}