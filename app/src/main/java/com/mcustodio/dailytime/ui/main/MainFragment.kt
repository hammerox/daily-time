package com.mcustodio.dailytime.ui.main

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.mcustodio.dailytime.*
import kotlinx.android.synthetic.main.main_fragment.view.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }



    private lateinit var viewModel: MainViewModel
    private val adapter by lazy { MainRecyclerAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.main_fragment, container, false)

        view.recycler_main.layoutManager = LinearLayoutManager(activity)
        view.recycler_main.adapter = adapter

        // Mock
//        adapter.userList = arrayListOf(User("mcustodio@stone.com.br", "Morrice", 78000L), User("pfrocha@stone.com.br", "Pedrinho", 92000L))
        adapter.onItemClick = {
            val intent = Intent(activity, TimerActivity::class.java)
            intent.putExtra("userKey", it.key())
            startActivity(intent)
        }

        view.fab_main.setOnClickListener {
            val intent = Intent(activity, UserActivity::class.java)
            startActivity(intent)
        }

        FirebaseDB.users.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}
            override fun onDataChange(data: DataSnapshot) {
                val users = data.children.mapNotNull { it.getValue(User::class.java) }
                adapter.userList = users.toList()
            }
        })

        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
    }
}
