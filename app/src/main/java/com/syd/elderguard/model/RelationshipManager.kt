package com.syd.elderguard.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RelationshipManager(
    var relationshipId: Long,
    val relationship: String,
    val relationshipCount: Int = 0,
    val peopleCount: Int = 0
) : Parcelable