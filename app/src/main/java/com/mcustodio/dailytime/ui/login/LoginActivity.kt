package com.mcustodio.dailytime.ui.login

import android.arch.lifecycle.Observer
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.mcustodio.dailytime.FirebaseDB
import com.mcustodio.dailytime.Preferences
import com.mcustodio.dailytime.R
import com.mcustodio.dailytime.data.User
import com.mcustodio.dailytime.ui.DbMockViewModel
import com.mcustodio.dailytime.ui.dailylist.DailyListActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Preferences(this).isLoggedIn = false

        button_login.setOnClickListener {
            val email = edit_login.text.toString().trim()
            FirebaseDB.onLoginAttempt(email) { usersFound ->
                usersFound.firstOrNull()?.let {
                    observeUser(it.id!!)
                } ?: Toast.makeText(this@LoginActivity, "Usuário não encontrado", Toast.LENGTH_LONG).show()
            }
        }

        val hasAlreadyLoggedIn = Preferences(this).loginUserId?.isNotBlank() == true
        loading_login.visibility = if (hasAlreadyLoggedIn) View.VISIBLE else View.GONE
        linear_login.visibility = if (!hasAlreadyLoggedIn) View.VISIBLE else View.GONE
        if (hasAlreadyLoggedIn) {
            observeUser(Preferences(this).loginUserId!!)
        }

        DbMockViewModel.loginUser.observe(this, Observer {user ->
            if (isNotLoggedIn(user)) {
                login(user!!)
            }
        })
    }

    fun observeUser(userId: String) {
        FirebaseDB.onUserChange(userId)
    }

    fun login(user: User) {
        Preferences(this).isLoggedIn = true
        Preferences(this).loginUserId = user.id
        val intent = Intent(this, DailyListActivity::class.java)
        startActivity(intent)
    }

    fun logout() {
        Preferences(this).isLoggedIn = false
        FirebaseDB.stopWatchingUser()
        DbMockViewModel.loginUser.value = null
        Preferences(this).loginUserId = null
    }

    fun isNotLoggedIn(user: User?) : Boolean {
        return user == null || Preferences(this).isLoggedIn?.not() ?: false
    }
}
