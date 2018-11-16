package com.mcustodio.dailytime.ui

import android.arch.lifecycle.MutableLiveData
import com.mcustodio.dailytime.FirebaseDB
import com.mcustodio.dailytime.data.Daily
import com.mcustodio.dailytime.data.Member
import com.mcustodio.dailytime.data.Team
import com.mcustodio.dailytime.data.User

object DbMockViewModel {

    val currentUser = MutableLiveData<User>()

    val teams = MutableLiveData<List<Team>>()
    val selectedTeam = MutableLiveData<Team>()

    val dailies = MutableLiveData<List<Daily>>()
    val activeDaily = MutableLiveData<Daily>()
    val selectedDaily = MutableLiveData<Daily>()

    val userMembers = MutableLiveData<List<Member>>()

    val members = MutableLiveData<List<Member>>()
    val selectedMember = MutableLiveData<Member>()


    // REPOSITORY
    fun saveAndFetchAllData(user: User) {
        currentUser.value = user
        val teamsId = user.saved_teams_to_member.keys.toList()

        FirebaseDB.setOnTeamsListener(teamsId) { teams ->
            this.teams.postValue(teams)
            fetchMembers(user, teamsId)
            fetchDailies(user, teamsId)
        }
    }

    private fun fetchMembers(user: User, teamsId: List<String>) {
        FirebaseDB.setOnMembersListener(teamsId) { members ->
            this.members.postValue(members)

            val userMembers = members.filter { user.saved_teams_to_member.values.contains(it.id) }
            this.userMembers.postValue(userMembers)
        }
    }

    private fun fetchDailies(user: User, teamsId: List<String>) {
        FirebaseDB.setOnDailiesListener(teamsId) {
            dailies.value = it
            activeDaily.value = dailies.value?.find { it.time_start != null && it.time_end == null }
            if (selectedDaily.value?.id != null) {
                val newSelectedDaily = dailies.value?.find { it.id == selectedDaily.value?.id }
                selectedDaily.postValue(newSelectedDaily)
            }
        }
    }


    fun changeTeam(selectedTeam: Team) {
        val currentMember = DbMockViewModel.findMember(selectedTeam)
        this.selectedTeam.value = selectedTeam
        this.selectedMember.value = currentMember
    }


    fun findMember(team: Team) : Member? {
        val memberId = this.currentUser.value?.saved_teams_to_member?.get(team.id)
        return userMembers.value?.find { it.id == memberId }
    }

}