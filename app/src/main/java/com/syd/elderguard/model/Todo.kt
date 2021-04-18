package com.syd.elderguard.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Todo(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var content: String,
    var dateTime: Long
) : Parcelable {
    constructor(content: String, dateTime: Long): this(0, content, dateTime)
}