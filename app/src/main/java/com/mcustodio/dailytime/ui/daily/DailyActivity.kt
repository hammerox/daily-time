package com.mcustodio.dailytime.ui.daily

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import com.mcustodio.dailytime.R
import com.mcustodio.dailytime.ui.daily.fragments.TaskFragment
import com.mcustodio.dailytime.ui.daily.fragments.TeamFragment
import com.mcustodio.dailytime.ui.daily.fragments.TimerFragment
import com.mcustodio.dailytime.ui.daily.fragments.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_daily.*

class DailyActivity : AppCompatActivity() {

    private val pagerAdapter by lazy { ViewPagerAdapter(supportFragmentManager) }
    private val taskFragment = TaskFragment()
    private val timerFragment = TimerFragment()
    private val teamFragment = TeamFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily)
        bottomnav_daily.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        setupViewPager()
    }

    private fun setupViewPager() {
        pagerAdapter.fragmentList.add(taskFragment)
        pagerAdapter.fragmentList.add(timerFragment)
        pagerAdapter.fragmentList.add(teamFragment)
        viewpager_daily.adapter = pagerAdapter
        viewpager_daily.addOnPageChangeListener(onViewPagerTabSelectedListener)
        viewpager_daily.currentItem = 1
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.item_daily_task -> {
                viewpager_daily.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.item_daily_timer -> {
                viewpager_daily.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
            R.id.item_daily_team -> {
                viewpager_daily.currentItem = 2
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private val onViewPagerTabSelectedListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(p0: Int) { }
        override fun onPageScrolled(p0: Int, p1: Float, p2: Int) { }
        override fun onPageSelected(pos: Int) {
            bottomnav_daily.menu.getItem(pos).isChecked = true
        }

    }
}
