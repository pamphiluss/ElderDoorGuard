package com.syd.elderguard.db

import androidx.room.*
import com.syd.elderguard.model.*

@Dao
interface FaceDao {
    @Query("DELETE FROM Face WHERE id = :id")
    fun deleteFace(id: Long)
}