package com.mcustodio.dailytime

import com.google.firebase.database.FirebaseDatabase
import java.util.*

object FirebaseDB {

    // Write a message to the database
    val root = FirebaseDatabase.getInstance().reference

    val usersKey = "users"
    val users = root.child(usersKey)



}