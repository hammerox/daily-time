package com.mcustodio.dailytime

import com.google.firebase.database.*
import com.mcustodio.dailytime.data.Daily
import com.mcustodio.dailytime.data.Member
import com.mcustodio.dailytime.data.Team
import com.mcustodio.dailytime.data.User

object FirebaseDB {

    val instance = FirebaseDatabase.getInstance()
    val root = instance.reference

    val usersKey = "users"
    val users = root.child(usersKey)
    var userQuery: Query? = null
    var userListener: ValueEventListener? = null

    fun setOnUserListener(email: String, onChange: (User?) -> Unit) {
        userListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) { }
            override fun onDataChange(data: DataSnapshot) {
                val user = data.children.mapNotNull {
                    val user = it.getValue(User::class.java)
                    user?.id = it.key
                    user
                }.firstOrNull()

                if (user != null) {
                    user.id = data.key
                } else {
                    removeAllListeners()
                }

                onChange(user)
            }
        }
        userQuery = FirebaseDB.users.orderByChild("email").equalTo(email).limitToFirst(1)
        userQuery?.addValueEventListener(userListener!!)
    }


    val teamsKey = "teams"
    val teams = root.child(teamsKey)
    var teamsListener: ValueEventListener? = null

    fun setOnTeamsListener(teamsId: List<String?>, onChange: (List<Team>) -> Unit) {
        teamsListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(data: DataSnapshot) {
                val teams = data.children.mapNotNull {
                    val team = it.getValue(Team::class.java)
                    team?.id = it.key
                    team
                }
                onChange(teams.toList().filter { teamsId.contains(it.id) })
            }
        }
        teams.addValueEventListener(teamsListener!!)
    }


    val membersKey = "members"
    val members = root.child(membersKey)
    var membersListener: ValueEventListener? = null

    fun setOnMembersListener(teamsId: List<String?>, onChange: (List<Member>) -> Unit) {
        membersListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(data: DataSnapshot) {
                val members = data.children.mapNotNull {
                    val member = it.getValue(Member::class.java)
                    member?.id = it.key
                    member
                }
                onChange(members.toList().filter { teamsId.contains(it.team_id) })
            }
        }
        // todo - Resolver "fetch all" de Members
        members.addValueEventListener(membersListener!!)
    }


    val dailiesKey = "dailies"
    val dailies = root.child(dailiesKey)
    var dailiesListener: ValueEventListener? = null

    fun setOnDailiesListener(teamsId: List<String?>, onChange: (List<Daily>) -> Unit) {
        dailiesListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(data: DataSnapshot) {
                val dailies = data.children.mapNotNull {
                    val daily = it.getValue(Daily::class.java)
                    daily?.id = it.key
                    daily
                }
                onChange(dailies.toList().filter { teamsId.contains(it.team_id) })
            }
        }
        // todo - Resolver "fetch all" de Dailies
        dailies.addValueEventListener(dailiesListener!!)
    }


    fun removeAllListeners() {
        userQuery?.removeEventListener(userListener!!)
        teams.removeEventListener(teamsListener!!)
        members.removeEventListener(membersListener!!)
        dailies.removeEventListener(dailiesListener!!)
    }



}