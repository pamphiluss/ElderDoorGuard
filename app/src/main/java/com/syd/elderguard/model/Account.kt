package com.syd.elderguard.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(foreignKeys = [ForeignKey(
    entity = Event::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("eventId"),
    onDelete = ForeignKey.CASCADE
), ForeignKey(
    entity = Relationship::class,
    parentColumns = arrayOf("id"),
    childColumns = arrayOf("relationshipId"),
    onDelete = ForeignKey.CASCADE)]
)
data class Account(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    val count: Float,//记账金额
    val outIntype: Int,//支出/收入 1：支出  2：收入
    val eventId: Long,//事件
    val people: String,//人物
    val relationshipId: Long,
    val date: Long,
    val place: String = "",
    val remark: String = "",
    var state: Int = -1 //0不需要上传，-1需要上传
) : Parcelable