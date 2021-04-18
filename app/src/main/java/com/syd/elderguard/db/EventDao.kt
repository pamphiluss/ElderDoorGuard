package com.syd.elderguard.db

import androidx.room.*
import com.syd.elderguard.model.Event

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEvent(event: Event)

    @Query("SELECT * FROM Event")
    fun getEventList(): List<Event>

    @Query("SELECT * FROM Event WHERE state<0 LIMIT 50")
    fun getEventUploadList(): List<Event>

    @Insert
    fun insertEventList(list: List<Event>)

    @Update
    fun updateEvent(vararg events: Event)

    @Query("select count(*) from Event where state<0")
    fun checkUnUploadCount(): Int

    @Query("DELETE FROM Event WHERE name = :name")
    fun deleteEventByName(name: String)
}