package com.mcustodio.dailytime

import com.google.firebase.database.*
import com.mcustodio.dailytime.data.Daily
import com.mcustodio.dailytime.data.Member
import com.mcustodio.dailytime.data.User
import com.mcustodio.dailytime.ui.DbMockViewModel

object FirebaseDB {

    val root = FirebaseDatabase.getInstance().reference

    val usersKey = "users"
    val users = root.child(usersKey)
    var loginUserReference: DatabaseReference? = null
    var loginUserListener: ValueEventListener? = null

    fun onLoginAttempt(email: String, onResult: (List<User>) -> Unit) {
        FirebaseDB.users.orderByChild("email").equalTo(email)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) { }
                override fun onDataChange(data: DataSnapshot) {
                    val users = data.children.mapNotNull {
                        val user = it.getValue(User::class.java)
                        user?.id = it.key
                        user
                    }
                    onResult(users)
                }
            })
    }

    fun onUserChange(userId: String) {
        loginUserListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) { }
            override fun onDataChange(data: DataSnapshot) {
                val user = data.getValue(User::class.java)
                user?.id = data.key
                DbMockViewModel.loginUser.value = user
            }
        }
        loginUserReference = FirebaseDatabase.getInstance().getReference("users/$userId")
        loginUserReference?.addValueEventListener(loginUserListener!!)
    }

    fun stopWatchingUser() {
        loginUserReference?.removeEventListener(loginUserListener!!)
    }

    val teamsKey = "teams"
    val teams = root.child(teamsKey)

    // todo - Remover mock team_id = -LRAIWUKQjOqTwGsJD1o
    val membersKey = "members"
    val members = root.child(membersKey).orderByChild("team_id").equalTo("-LRAIWUKQjOqTwGsJD1o").ref
    fun onMembersChange(onChange: (List<Member>) -> Unit) {
        members.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(data: DataSnapshot) {
                val members = data.children.mapNotNull {
                    val member = it.getValue(Member::class.java)
                    member?.id = it.key
                    member
                }
                onChange(members.toList())
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