package com.syd.elderguard.model

import android.os.Parcelable
import cn.bmob.v3.BmobObject
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AccountBmob(
    val count: Float,//记账金额
    val outIntype: Int,//支出/收入 1：支出  2：收入
    val eventId: Long,//事件
    val people: String,//人物
    val relationshipId: Long,
    val date: Long,
    val place: String = "",
    val remark: String = "",
    val userId: String
) : BmobObject(), Parcelable {
    constructor(): this(0F, 0, 0,
        "", 0, 0, "", "", "")
}