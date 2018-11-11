package com.mcustodio.dailytime

data class User(var email: String? = null, var nickname: String? = null, var lastTimer: Long? = null) {

    fun key() : String {
        return email!!
            .replace("@", "-")
            .replace(".", "-")
    }
}