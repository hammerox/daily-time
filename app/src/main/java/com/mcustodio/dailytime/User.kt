package com.mcustodio.dailytime

data class User(var email: String? = null, var nickname: String? = null, var time: Long? = null) {

    fun key() : String {
        return email!!
            .replace("@", "-")
            .replace(".", "-")
    }
}