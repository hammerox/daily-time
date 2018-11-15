package com.mcustodio.dailytime

import android.content.Context
import android.content.SharedPreferences

class Preferences(context: Context) {

    val PREFS_FILENAME = "com.mcustodio.dailytime"
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0)


    private val WHAT_YOU_DID = "WHAT_YOU_DID"
    var whatYouDid: String?
        get() = prefs.getString(WHAT_YOU_DID, "")
        set(value) = prefs.edit().putString(WHAT_YOU_DID, value).apply()

    private val WHAT_YOU_WILL_DO = "WHAT_YOU_WILL_DO"
    var whatYouWillDo: String?
        get() = prefs.getString(WHAT_YOU_WILL_DO, "")
        set(value) = prefs.edit().putString(WHAT_YOU_WILL_DO, value).apply()

    private val DIFFICULTIES = "DIFFICULTIES"
    var difficulties: String?
        get() = prefs.getString(DIFFICULTIES, "")
        set(value) = prefs.edit().putString(DIFFICULTIES, value).apply()

    private val LAST_SELECTED_TEAM = "LAST_SELECTED_TEAM"
    var lastSelectedTeam: String?
        get() = prefs.getString(LAST_SELECTED_TEAM, "")
        set(value) = prefs.edit().putString(LAST_SELECTED_TEAM, value).apply()


    fun clearAll() {
        prefs.edit().clear().apply()
    }


}