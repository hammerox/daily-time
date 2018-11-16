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
            validateUser(email)
        }

        val wasAlreadyLoggedIn = Preferences(this).loginUserId?.isNotBlank() == true
        loading_login.visibility = if (wasAlreadyLoggedIn) View.VISIBLE else View.GONE
        linear_login.visibility = if (!wasAlreadyLoggedIn) View.VISIBLE else View.GONE

        if (wasAlreadyLoggedIn) {
            validateUser(Preferences(this).loginUserId!!)
        }

        DbMockViewModel.loginUser.observe(this, Observer {user ->
            if (isNotLoggedIn(user)) {
                login(user!!)
            }
        })
    }

    fun validateUser(email: String) {
        FirebaseDB.setOnUserListener(email) { user ->
            user?.let {
                DbMockViewModel.loginUser.value = user

            } ?: Toast.makeText(this@LoginActivity, "Usuário não encontrado", Toast.LENGTH_LONG).show()
        }
    }

    fun login(user: User) {
        Preferences(this).isLoggedIn = true
        Preferences(this).loginUserId = user.email
        DbMockViewModel.fetchAllData(user)
        val intent = Intent(this, DailyListActivity::class.java)
        startActivity(intent)
    }

    fun logout() {
        Preferences(this).isLoggedIn = false
        Preferences(this).loginUserId = null
        DbMockViewModel.loginUser.value = null
        FirebaseDB.removeAllListeners()
    }

    fun isNotLoggedIn(user: User?) : Boolean {
        return user == null || Preferences(this).isLoggedIn?.not() ?: false
    }
}
