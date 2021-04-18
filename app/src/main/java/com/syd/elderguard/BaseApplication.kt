package com.syd.elderguard

import android.app.Application
import cn.bmob.v3.Bmob
import com.androidnetworking.AndroidNetworking
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.syd.elderguard.di.dbModule
import com.syd.elderguard.di.repositoryModule
import com.syd.elderguard.di.viewModelModule
import com.tencent.mmkv.MMKV
import com.tencent.mmkv.MMKVHandler
import com.tencent.mmkv.MMKVLogLevel
import com.tencent.mmkv.MMKVRecoverStrategic
import es.dmoral.toasty.Toasty
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class BaseApplication : Application() {

    companion object {
        @JvmStatic lateinit var inst: BaseApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()
        inst = this

        initMMKV()
        initKoin()
        initNetwork()
        initBmob()
        initToast()

    }



    private fun initToast() {
        Toasty.Config.getInstance()
            .setTextSize(12)
            .allowQueue(false)
            .apply()
    }

    private fun initMMKV() {
        MMKV.initialize(this)
        MMKV.registerHandler(object : MMKVHandler {
            override fun onMMKVCRCCheckFail(p0: String?): MMKVRecoverStrategic {
                return MMKVRecoverStrategic.OnErrorRecover
            }

            override fun wantLogRedirecting(): Boolean {
                return false
            }

            override fun mmkvLog(
                p0: MMKVLogLevel?,
                p1: String?,
                p2: Int,
                p3: String?,
                p4: String?
            ) {
                TODO("Not yet implemented")
            }

            override fun onMMKVFileLengthError(p0: String?): MMKVRecoverStrategic {
                return MMKVRecoverStrategic.OnErrorRecover
            }
        })
    }

    private fun initBmob() {
        Bmob.initialize(this, "06685ef5ec8a8c04206ab0a5c20cf50c")
    }

    private fun initNetwork() {
        Stetho.initializeWithDefaults(this)
        val okHttpClient = OkHttpClient().newBuilder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()
        AndroidNetworking.initialize(applicationContext, okHttpClient)
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@BaseApplication)
           // modules(networkModule)
            modules(dbModule)
            modules(repositoryModule)
            modules(viewModelModule)
        }
    }
}