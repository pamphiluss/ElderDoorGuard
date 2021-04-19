package com.syd.elderguard.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Face(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val Name: String,
    val Sex: Int,
    val Birthday: Long,
    val Relati0nshipID: Long,
    val remark: String = "",
    var state: Int = -1 //0不需要上传，-1需要上传
) : Parcelable