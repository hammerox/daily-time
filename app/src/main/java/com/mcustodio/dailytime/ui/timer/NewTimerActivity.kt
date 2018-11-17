package com.mcustodio.dailytime.ui.timer

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.mcustodio.dailytime.R
import com.mcustodio.dailytime.ui.timer.fragments.TaskFragment
import com.mcustodio.dailytime.ui.timer.fragments.TeamFragment
import com.mcustodio.dailytime.ui.timer.fragments.DailyFragment
import com.mcustodio.dailytime.ui.timer.fragments.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_newtimer.*

class NewTimerActivity : AppCompatActivity() {

    private val pagerAdapter by lazy { ViewPagerAdapter(supportFragmentManager) }
    private val taskFragment = TaskFragment()
    private val dailyFragment = DailyFragment()
    private val teamFragment = TeamFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newtimer)
        setupViewPager()
        bottomnav_newtimer.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
    }

    private fun setupViewPager() {
        pagerAdapter.fragmentList.add(taskFragment)
        pagerAdapter.fragmentList.add(dailyFragment)
        pagerAdapter.fragmentList.add(teamFragment)
        viewpager_newtimer.adapter = pagerAdapter
        viewpager_newtimer.addOnPageChangeListener(onViewPagerTabSelectedListener)
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.item_newtimer_task -> {
                viewpager_newtimer.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.item_newtimer_daily -> {
                viewpager_newtimer.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
            R.id.item_newtimer_team -> {
                viewpager_newtimer.currentItem = 2
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private val onViewPagerTabSelectedListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(p0: Int) { }
        override fun onPageScrolled(p0: Int, p1: Float, p2: Int) { }
        override fun onPageSelected(pos: Int) {
            bottomnav_newtimer.menu.getItem(pos).isChecked = true
        }

    }
}
