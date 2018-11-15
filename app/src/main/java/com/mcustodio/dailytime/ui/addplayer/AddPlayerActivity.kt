package com.mcustodio.dailytime.ui.addplayer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mcustodio.dailytime.FirebaseDB
import com.mcustodio.dailytime.R
import com.mcustodio.dailytime.data.Player
import kotlinx.android.synthetic.main.activity_addplayer.*

class AddPlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addplayer)
        button_user_save.setOnClickListener {
            val user = Player(edit_user_nickname.text.toString(), null, null, null)
            FirebaseDB.players.push().setValue(user)
                .addOnSuccessListener { finish() }
                .addOnFailureListener { Toast.makeText(this, it.message, Toast.LENGTH_LONG).show() }
        }
    }
}
