package com.mcustodio.dailytime

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        button_user_save.setOnClickListener {
            val user = User(edit_user_nickname.text.toString(), null, null,null)
            FirebaseDB.users.push().setValue(user)
                .addOnSuccessListener { finish() }
                .addOnFailureListener { Toast.makeText(this, it.message, Toast.LENGTH_LONG).show() }
        }
    }
}
