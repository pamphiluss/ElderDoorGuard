package com.syd.elderguard.ui.activity

import android.graphics.Paint
import android.os.Bundle
import android.view.View
import com.gyf.immersionbar.ImmersionBar
import com.syd.elderguard.R
import com.syd.elderguard.ui.base.BaseActivity
import com.syd.elderguard.utils.toAgreement
import com.syd.elderguard.utils.toOutBrowser
import com.syd.elderguard.utils.toPrivacy
import kotlinx.android.synthetic.main.activity_about_us.*


class AboutUsActivity : BaseActivity(), View.OnClickListener {

    override fun getLayoutId(): Int {
        return R.layout.activity_about_us
    }

    override fun initImmersionBar() {
        ImmersionBar.with(this)
            .navigationBarColor(R.color.common_background)
            .statusBarDarkFont(true)
            .init()
    }

    override fun getToolbarTitleId(): Int {
        return R.string.title_about
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        txvAppNameVersion.text = "${resources.getString(R.string.app_name)}-v${getVersionName()}"

        txvHistoryVersionList.paint.flags = Paint.UNDERLINE_TEXT_FLAG; //下划线
        txvHistoryVersionList.paint.isAntiAlias = true;//抗锯齿

        txvHistoryVersionList.setOnClickListener(this)
        txvUserAgreement.setOnClickListener(this)
        txvUserPrivacy.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.txvHistoryVersionList-> {
                toOutBrowser(this@AboutUsActivity, "http://www.striveyadong.com")
            }

            R.id.txvUserAgreement-> {
                toAgreement(this@AboutUsActivity)
            }

            R.id.txvUserPrivacy-> {
                toPrivacy(this@AboutUsActivity)
            }
        }
    }

    private fun getVersionName(): String {
        // 获取packagemanager的实例
        val packageManager = packageManager
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        val packInfo = packageManager.getPackageInfo(packageName, 0)
        return packInfo.versionName
    }
}