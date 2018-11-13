package com.mcustodio.dailytime

import com.google.firebase.database.FirebaseDatabase
import java.util.*

object FirebaseDB {

    // Write a message to the database
    val root = FirebaseDatabase.getInstance().reference

    val teamsKey = "teams"
    val teams = root.child(teamsKey)

    val usersKey = "users"
    val users = root.child(usersKey)

    val dailiesKey = "dailies"
    val dailies = root.child(dailiesKey)



}