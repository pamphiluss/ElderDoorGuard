package com.syd.elderguard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.syd.elderguard.ui.fragment.DashboardFragment
import com.syd.elderguard.ui.fragment.HomeFragment
import com.syd.elderguard.ui.fragment.MineFragment
import com.syd.elderguard.ui.fragment.RelationshipFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val fragments = arrayOf(
        HomeFragment(), RelationshipFragment(),
        DashboardFragment(), MineFragment()
    )

    private var lastfragment = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBottomNav()

        //设置fragment到布局
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameMain, fragments[0])
            .show(fragments[0]).commit();
    }

    /**
     * bottom监听
     */
    private fun initBottomNav() {
        navView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_home -> {
                    switchFragment(lastfragment, 0)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigation_relationship -> {
                    switchFragment(lastfragment, 1)
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.navigation_dashboard -> {
                    switchFragment(lastfragment, 2)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_mine -> {
                    switchFragment(lastfragment, 3)
                    return@setOnNavigationItemSelectedListener true
                }
                else -> return@setOnNavigationItemSelectedListener false
            }
            return@setOnNavigationItemSelectedListener false
        }
    }

    /***
     * fragment切换
     * @param oldFragment
     * @param
     */
    private fun switchFragment(oldFragment: Int, index: Int) {
        if(lastfragment == index) {
            return;
        }

        val fragmentTransaction = supportFragmentManager.beginTransaction()

        fragmentTransaction.hide(fragments[oldFragment])
        if(!fragments[index].isAdded) {
            fragmentTransaction.add(R.id.frameMain, fragments[index])
        }

        fragmentTransaction.show(fragments[index]).commitNowAllowingStateLoss()

        //最后一次点击
        lastfragment = index
    }
}
