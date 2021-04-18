package com.syd.elderguard.ui.activity

import android.os.Bundle
import android.view.View
import com.gyf.immersionbar.ImmersionBar
import com.syd.elderguard.R
import com.syd.elderguard.ui.base.BaseActivity
import com.syd.elderguard.utils.toGiveFiveStar
import kotlinx.android.synthetic.main.activity_ohter_apps.*
import kotlinx.android.synthetic.main.dialog_user_guide.*

class OtherAppsActivity : BaseActivity(), View.OnClickListener {

    override fun getLayoutId(): Int {
        return R.layout.activity_ohter_apps
    }

    override fun getToolbarTitleId(): Int {
        return R.string.title_ohter_apps
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
            .navigationBarColor(R.color.common_background)
            .statusBarDarkFont(true)
            .init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rlChanApp.setOnClickListener(this)
        rlChunApp.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        p0?.let {
            when(p0.id) {
                R.id.rlChanApp-> {
                    toGiveFiveStar(this@OtherAppsActivity, "com.rest.phone")
                }
                R.id.rlChunApp-> {
                    toGiveFiveStar(this@OtherAppsActivity, "com.simple.colorful")
                }

            }
        }
    }
}