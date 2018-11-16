package com.mcustodio.dailytime.ui

import android.arch.lifecycle.MutableLiveData
import com.mcustodio.dailytime.FirebaseDB
import com.mcustodio.dailytime.data.Daily
import com.mcustodio.dailytime.data.Member
import com.mcustodio.dailytime.data.Team
import com.mcustodio.dailytime.data.User

object DbMockViewModel {

    val loginUser = MutableLiveData<User>()
    val selectedTeam = MutableLiveData<Team>()

    val dailies = MutableLiveData<List<Daily>>()
    val activeDaily = MutableLiveData<Daily>()
    val selectedDaily = MutableLiveData<Daily>()

    val members = MutableLiveData<List<Member>>()
    val selectedMember = MutableLiveData<Member>()


    init {
        // todo - Remover mock
        selectedTeam.value = Team(name = "Salesforce", code = "I2AK87", id = "-LRAIWUKQjOqTwGsJD1o")

        FirebaseDB.onDailiesChange {
            dailies.value = it
            activeDaily.value = dailies.value?.find { it.time_start != null && it.time_end == null }
            if (selectedDaily.value?.id != null) {
                val newSelectedDaily = dailies.value?.find { it.id == selectedDaily.value?.id }
                selectedDaily.postValue(newSelectedDaily)
            }
        }

        FirebaseDB.onMembersChange {
            members.postValue(it)
        }
    }

}