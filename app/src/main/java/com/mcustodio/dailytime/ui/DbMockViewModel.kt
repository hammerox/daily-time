package com.mcustodio.dailytime.ui

import android.arch.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.mcustodio.dailytime.FirebaseDB
import com.mcustodio.dailytime.data.Daily
import com.mcustodio.dailytime.data.Member
import com.mcustodio.dailytime.data.Team
import com.mcustodio.dailytime.data.User
import java.util.*

object DbMockViewModel {

    val currentUser = MutableLiveData<User>()

    val teams = MutableLiveData<List<Team>>()
    val selectedTeam = MutableLiveData<Team>()

    val dailies = MutableLiveData<List<Daily>>()
    val selectedDaily = MutableLiveData<Daily>()

    val userMembers = MutableLiveData<List<Member>>()

    val members = MutableLiveData<List<Member>>()
    val selectedMember = MutableLiveData<Member>()
    val isAdmin = MutableLiveData<Boolean>()


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
        FirebaseDB.setOnDailiesListener(teamsId) { fetchedDailies ->
            dailies.value = fetchedDailies

            // Atualiza daily selecionada
            if (selectedDaily.value?.id != null) {
                selectedDaily.value = fetchedDailies.find { it.id == selectedDaily.value?.id }

            // Seleciona daily recém-aberta
            } else if (selectedTeam.value != null && selectedDaily.value?.id == null) {
                val newSelectedDaily = dailies.value?.find { it.status == Daily.Status.Started }
                selectedDaily.postValue(newSelectedDaily)
            }
        }
    }


    // TeamListViewModel

    fun changeTeam(selectedTeam: Team) {
        val currentMember = DbMockViewModel.findMember(selectedTeam)
        this.selectedTeam.value = selectedTeam
        this.selectedMember.value = currentMember
        this.isAdmin.value = currentMember?.admin ?: false
        this.selectedDaily.value = dailies.value?.find { d -> d.team_id == selectedTeam.id && d.status == Daily.Status.Started }
    }


    fun findMember(team: Team) : Member? {
        val memberId = this.currentUser.value?.saved_teams_to_member?.get(team.id)
        return userMembers.value?.find { it.id == memberId }
    }


    // DailyViewModel

    fun getElapsedTime() : Long {
        val memberId = this.selectedMember.value?.id
        return this.selectedDaily.value?.members_time?.get(memberId) ?: 0
    }

    fun isSpeaking(isSpeaking: Boolean): Task<Void> {
        val memberId = selectedMember.value?.id
        val reference = FirebaseDB.instance.getReference("members/$memberId/")
        return reference.updateChildren(hashMapOf("speaking" to isSpeaking as Any))
    }

    fun chooseRandomMemberToSpeak(): Member? {
        val membersThatAlreadySpoken = DbMockViewModel.selectedDaily.value?.members_time?.keys ?: hashSetOf()
        return DbMockViewModel.members.value
            ?.filter { !membersThatAlreadySpoken.contains(it.id) }
            ?.run {
                val randomIndex = Random().nextInt(this.size)
                this[randomIndex]
            }
    }

    fun createDaily(): Task<Void> {
        val newDaily = Daily.create(selectedTeam.value)
        return FirebaseDB.dailies.ref.push().setValue(newDaily)
    }

    fun closeDaily(): Task<Void> {
        val daily = selectedDaily.value
        daily?.time = Calendar.getInstance().time.time - daily?.started_at!!
        val values = hashMapOf("time" to daily.time as Any)
        val reference = FirebaseDB.instance.getReference("dailies/${daily.id}/")
        return reference.updateChildren(values)
    }

    fun saveMemberTime(time: Long): Task<Void> {
        val dailyId = selectedDaily.value?.id
        val memberId = selectedMember.value?.id
        val reference = FirebaseDB.instance.getReference("dailies/$dailyId/members_time/")
        return reference.updateChildren(hashMapOf(memberId to time as Any))
    }

}