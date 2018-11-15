package com.mcustodio.dailytime.ui

import android.arch.lifecycle.MutableLiveData
import com.mcustodio.dailytime.FirebaseDB
import com.mcustodio.dailytime.data.Daily
import com.mcustodio.dailytime.data.Player
import com.mcustodio.dailytime.data.Team

object DbMockViewModel {

    var selectedTeam = MutableLiveData<Team>()

    var dailies = MutableLiveData<List<Daily>>()
    var selectedDaily = MutableLiveData<Daily>()

    var players = MutableLiveData<List<Player>>()
    var selectedPlayer = MutableLiveData<Player>()


    init {
        // todo - Remover mock
        selectedTeam.value = Team(name = "Salesforce", code = "I2AK87", id = "-LRAIWUKQjOqTwGsJD1o")

        FirebaseDB.onDailiesChange {
            dailies.value = it
            if (selectedDaily.value?.id != null) {
                val newSelectedDaily = dailies.value?.find { it.id == selectedDaily.value?.id }
                selectedDaily.postValue(newSelectedDaily)
            }
        }

        FirebaseDB.onPlayersChange {
            players.postValue(it)
        }
    }

}