package com.syd.elderguard.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AccountManager(
    val id: Long,
    val count: Float,//记账金额
    val outIntype: Int,//支出/收入 1：支出  2：收入
    val eventId: Long,//事件
    val people: String,//人物
    val relationshipId: Long,
    val date: Long,
    val place: String = "",
    val remark: String = "",
    val eventName: String,//事件
    val relationshipName: String
) : Parcelable