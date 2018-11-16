package com.mcustodio.dailytime.ui.addmember

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.mcustodio.dailytime.FirebaseDB
import com.mcustodio.dailytime.R
import com.mcustodio.dailytime.data.Member
import kotlinx.android.synthetic.main.activity_addmember.*

class AddMemberActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addmember)
        button_user_save.setOnClickListener {
            val user = Member(edit_user_nickname.text.toString(), null, null, null)
            FirebaseDB.members.push().setValue(user)
                .addOnSuccessListener { finish() }
                .addOnFailureListener { Toast.makeText(this, it.message, Toast.LENGTH_LONG).show() }
        }
    }
}
