package com.syd.elderguard.ui.base

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.gyf.immersionbar.ImmersionBar
import com.syd.elderguard.R
import es.dmoral.toasty.Toasty


abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId());
        //初始化沉浸式
        initImmersionBar();
        initToolbar();
        //初始化数据
        initData();
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    abstract fun getLayoutId(): Int

    open fun initImmersionBar() {
        ImmersionBar.with(this)
            .navigationBarColor(android.R.color.white)
            .statusBarDarkFont(true)
            .init()
    }

    open fun initData() {

    }

    private fun initToolbar() {
        if (!showToolbar()) return

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setTitle(getToolbarTitleId())
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true);//左侧添加一个默认的返回图标
        supportActionBar?.setHomeButtonEnabled(true); //设置返回键可用
    }

    open fun showToolbar(): Boolean {
        return true
    }

    open fun getToolbarTitleId(): Int {
        return R.string.app_name;
    }

    fun showLongToast(message: String) {
        Toasty.normal(this, message).show();
    }

    open fun showLongToast(@StringRes resId: Int) {
        Toasty.normal(this, getString(resId)).show()
    }

}