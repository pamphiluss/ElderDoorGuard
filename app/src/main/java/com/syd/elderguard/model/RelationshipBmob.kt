package com.syd.elderguard.model

import android.os.Parcelable
import cn.bmob.v3.BmobObject
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RelationshipBmob(
    val name: String?,
    val userId: String?
) : BmobObject(), Parcelable {

    constructor(): this(null, null)
}