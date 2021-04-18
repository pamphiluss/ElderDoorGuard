package com.syd.elderguard.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Event(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val name: String,
    var state: Int = 0 //0不需要上传，-1需要上传
) : Parcelable {

    constructor(name: String, state: Int = 0): this(0, name, state)
}