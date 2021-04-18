package com.syd.elderguard.utils

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import com.syd.elderguard.Constant
import com.syd.elderguard.ui.activity.*

fun toGuide(activity: Activity) {
    val intent = Intent(activity, GuideActivity().javaClass)
    activity.startActivity(intent)
}

fun toMain(activity: Activity) {
    val intent = Intent(activity, MainActivity().javaClass)
    activity.startActivity(intent)
}


fun toAddFace(activity: Activity, accountType: Int) {
    val intent = Intent(activity, AddFace().javaClass)
    intent.putExtra(Constant.BUNDLE_ACCOUNT_TYPE, accountType);
    activity.startActivity(intent)
}

fun toLogin(activity: Activity) {
    val intent = Intent(activity, LoginActivity().javaClass)
    activity.startActivity(intent)
}

fun toUserInfoAct(activity: Activity) {
    val intent = Intent(activity, UserInfoActivity().javaClass)
    activity.startActivity(intent)
}

/**
 * 事件列表
 */
fun toEventList(activity: Activity) {
    val intent = Intent(activity, EventListActivity().javaClass)
    activity.startActivityForResult(intent, Activity.RESULT_OK)
}

fun toAgreement(activity: Activity) {
    val intent = Intent(activity, CommonWebActivity::class.java)
    intent.putExtra("title", "用户协议")
    intent.putExtra("url", "file:///android_asset/agreement.html")
    activity.startActivity(intent)
}

fun toPrivacy(activity: Activity) {
    val intent = Intent(activity, CommonWebActivity::class.java)
    intent.putExtra("title", "隐私政策")
    intent.putExtra("url", "file:///android_asset/privacy.html")
    activity.startActivity(intent)
}

fun toOutBrowser(activity: Activity, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    activity.startActivity(intent)
}

fun toGiveFiveStar(activity: Activity?, packageNmae: String) {
    if (activity == null) return
    try {
        val uri =
            Uri.parse("market://details?id=${packageNmae}")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivity(intent)
    } catch (e: Exception) {
        Toast.makeText(activity, "您的手机没有安装Android应用市场", Toast.LENGTH_SHORT).show()
        e.printStackTrace()
    }
}

fun joinQQGroup(activity: Activity?): Boolean {
    if (activity == null) return false
    val key = "WZHW6UuzHptFKhh9BpMt3cY_wr5GzDLf"
    val intent = Intent()
    intent.data =
        Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D$key")
    // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    return try {
        activity.startActivity(intent)
        true
    } catch (e: java.lang.Exception) {
        // 未安装手Q或安装的版本不支持
        Toast.makeText(activity, "未安装手Q或安装的版本不支持,群号已复制", Toast.LENGTH_LONG).show()
        copy(activity, "254835796")
        false
    }
}

fun copy(activity: Activity, content: String?) {
    //获取剪贴板管理器：
    val cm: ClipboardManager =
        activity.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    //创建普通字符型ClipData
    val mClipData = ClipData.newPlainText("Label", content)
    //将ClipData内容放到系统剪贴板里。
    cm.setPrimaryClip(mClipData)
}
