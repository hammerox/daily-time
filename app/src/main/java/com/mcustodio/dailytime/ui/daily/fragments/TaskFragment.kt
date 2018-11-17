package com.mcustodio.dailytime.ui.daily.fragments


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mcustodio.dailytime.Preferences

import com.mcustodio.dailytime.R
import kotlinx.android.synthetic.main.fragment_daily_task.view.*


class TaskFragment : Fragment() {

    private val preferences by lazy { Preferences(activity!!) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_daily_task, container, false)

        view.edit_dailytask_whatyoudid.setText(preferences.whatYouDid)
        view.edit_dailytask_whatyouwilldo.setText(preferences.whatYouWillDo)
        view.edit_dailytask_difficulties.setText(preferences.difficulties)

        return view
    }

    override fun onPause() {
        super.onPause()
        preferences.whatYouDid = view?.edit_dailytask_whatyoudid?.text.toString()
        preferences.whatYouWillDo = view?.edit_dailytask_whatyouwilldo?.text.toString()
        preferences.difficulties = view?.edit_dailytask_difficulties?.text.toString()
    }


}
