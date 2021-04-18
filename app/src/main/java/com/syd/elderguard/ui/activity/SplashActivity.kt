package com.syd.elderguard.ui.activity

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView

import androidx.appcompat.app.AlertDialog;

import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.syd.elderguard.Constant
import com.syd.elderguard.R
import com.syd.elderguard.ui.base.BaseActivity
import com.syd.elderguard.utils.toAgreement
import com.syd.elderguard.utils.toGuide
import com.syd.elderguard.utils.toMain
import com.syd.elderguard.utils.toPrivacy
import com.silencedut.taskscheduler.TaskScheduler
import com.tencent.mmkv.MMKV


class SplashActivity : BaseActivity() {

    override fun getLayoutId() = R.layout.activity_splash

    override fun showToolbar() = false

    override fun initImmersionBar() {
        ImmersionBar.with(this)
            .navigationBarColor(android.R.color.white)
            .statusBarDarkFont(true)
            .hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR)
            .init()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val isFirst = MMKV.defaultMMKV().decodeBool(Constant.SP_IS_FIRST, true)
        val isShowUserGuide = MMKV.defaultMMKV().decodeBool(Constant.SP_IS_SHOW_USER_GUIDE, true)
        if (isShowUserGuide) {
            TaskScheduler.runOnUIThread({ showUserGuideDialog() }, 1500L)
            return
        }

        var delayTime = 500L
        if (isFirst) delayTime = 3000L

        TaskScheduler.runOnUIThread({
            if (isFirst) {
                toGuide(this@SplashActivity)
                finish()
            } else {
                toMain(this@SplashActivity)
                finish()
            }
        }, delayTime)
    }

    private fun showUserGuideDialog() {
        val view: View = LayoutInflater.from(this).inflate(R.layout.dialog_user_guide, null, false)
        val dialog: AlertDialog = AlertDialog.Builder(this).setView(view).create()
        dialog.setCanceledOnTouchOutside(false)
        val btnUserRefuse: Button = view.findViewById(R.id.btnUserRefuse)
        val btnUserAgree: Button = view.findViewById(R.id.btnUserAgree)

        btnUserRefuse.setOnClickListener {
            finish()
            dialog.dismiss()
        }

        btnUserAgree.setOnClickListener {
            MMKV.defaultMMKV().encode(Constant.SP_IS_SHOW_USER_GUIDE, false)
            toGuide(this@SplashActivity)
            dialog.dismiss()
        }

        val txvUserToDetail: TextView = view.findViewById(R.id.txvUserToDetail)
        val spannableString = SpannableString("可点击《隐私政策》和《用户协议》查看完成内容。")
        val foregroundColorSpan =
            ForegroundColorSpan(resources.getColor(R.color.colorAccent))
        spannableString.setSpan(foregroundColorSpan, 3, 9, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

        val foregroundColorSpan2 =
            ForegroundColorSpan(resources.getColor(R.color.colorAccent))
        spannableString.setSpan(foregroundColorSpan2, 10, 16, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

        val clickableSpan: ClickableSpan = object : ClickableSpan() {

            override fun onClick(p0: View) {
                toPrivacy(this@SplashActivity)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
            }
        }
        spannableString.setSpan(clickableSpan, 3, 9, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

        val clickableSpan1: ClickableSpan = object : ClickableSpan() {

            override fun onClick(p0: View) {
                toAgreement(this@SplashActivity)
            }

            override fun updateDrawState(ds: TextPaint) {
                ds.isUnderlineText = false
            }
        }

        spannableString.setSpan(clickableSpan1, 10, 16, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)

        txvUserToDetail.movementMethod = LinkMovementMethod.getInstance()
        txvUserToDetail.text = spannableString

        dialog.show()
    }

}