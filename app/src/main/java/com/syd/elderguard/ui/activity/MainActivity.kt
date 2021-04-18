package com.syd.elderguard.ui.activity

import com.syd.elderguard.R
import com.syd.elderguard.ui.base.BaseActivity
import com.syd.elderguard.ui.dialog.ActionSheetDialog
import com.syd.elderguard.ui.fragment.DashboardFragment
import com.syd.elderguard.ui.fragment.HomeFragment
import com.syd.elderguard.ui.fragment.MineFragment
import com.syd.elderguard.ui.fragment.RelationshipFragment
import com.syd.elderguard.utils.toAddAccount
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.system.exitProcess


class MainActivity : BaseActivity() {

    private val fragments = arrayOf(
        HomeFragment(), RelationshipFragment(),
        DashboardFragment(), MineFragment()
    )

    //上一次点击的页面
    private var lastfragment = 0
    private var mPressedTime:Long = 0

    override fun getLayoutId(): Int {
        return R.layout.activity_main;
    }

    override fun showToolbar(): Boolean {
        return false
    }

    override fun initData() {
        super.initData()
        initBottomNav()

        //设置fragment到布局
        supportFragmentManager.beginTransaction().replace(R.id.frameMain, fragments[0])
            .show(fragments[0]).commit()

    }

    override fun onBackPressed() {
        val mNowTime = System.currentTimeMillis() //获取第一次按键时间

        if (mNowTime - mPressedTime > 2000) { //比较两次按键时间差
            showLongToast("再按一次退出程序")
            mPressedTime = mNowTime
        } else { //退出程序
            finish()
            exitProcess(0)
        }
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
                else -> {
                    showFaceDataKeepingDialog()
                    return@setOnNavigationItemSelectedListener false
                }
            }
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

    private fun showFaceDataKeepingDialog() {
        ActionSheetDialog(this)
            .builder()
            .setCancelable(true)
            .setCanceledOnTouchOutside(true)
            .addSheetItem("添加人脸数据",
                ActionSheetDialog.SheetItemColor.Red
            ) {
                toAddAccount(this@MainActivity, 1)
            }.addSheetItem("查看已有人脸库",
                ActionSheetDialog.SheetItemColor.Red
            ) {
                toAddAccount(this@MainActivity,2)
            }.show()
    }

}
