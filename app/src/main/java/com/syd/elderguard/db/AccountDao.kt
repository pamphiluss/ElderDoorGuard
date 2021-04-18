package com.syd.elderguard.db

import androidx.room.*
import com.syd.elderguard.model.*


@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEvent(account: Account)

    @Query("DELETE FROM Account WHERE id = :id")
    fun deleteAccount(id: Long)

    @Query("SELECT Account.*, Relationship.name as relationshipName, Event.name as eventName FROM Account, Relationship, Event ON Account.relationshipId =Relationship.id AND Account.eventId = Event.id WHERE outIntype = :outIntype ORDER BY date DESC")
    fun getAccountListByOutIntypeDescDate(outIntype: Int): List<AccountManager>

    @Query("SELECT Account.*, Relationship.name as relationshipName, Event.name as eventName FROM Account, Relationship, Event ON Account.relationshipId =Relationship.id AND Account.eventId = Event.id WHERE outIntype = :outIntype ORDER BY date ASC")
    fun getAccountListByOutIntypeAscDate(outIntype: Int): List<AccountManager>

    @Query("SELECT Account.*, Relationship.name as relationshipName, Event.name as eventName FROM Account, Relationship, Event ON Account.relationshipId =Relationship.id AND Account.eventId = Event.id WHERE outIntype = :outIntype ORDER BY count DESC")
    fun getAccountListByOutIntypeDescCount(outIntype: Int): List<AccountManager>

    @Query("SELECT Account.*, Relationship.name as relationshipName, Event.name as eventName FROM Account, Relationship, Event ON Account.relationshipId =Relationship.id AND Account.eventId = Event.id WHERE outIntype = :outIntype ORDER BY count ASC")
    fun getAccountListByOutIntypeAscCount(outIntype: Int): List<AccountManager>

    //like拼接 https://www.e-learn.cn/content/wangluowenzhang/191617
    @Query("SELECT Account.*, Relationship.name as relationshipName, Event.name as eventName FROM Account, Relationship, Event ON Account.relationshipId =Relationship.id AND Account.eventId = Event.id WHERE people LIKE '%' || :word || '%' OR Event.name LIKE '%' || :word || '%' OR Relationship.name LIKE '%' || :word || '%'")
    fun getAccountListByKeyword(word: String): List<AccountManager>

    @Query("select sum(count) AS sum, outIntype from Account group by outIntype")
    fun accountSumByIntype(): List<CountSum>

    @Query("select count(distinct people) from account where outIntype = 1")
    fun outPeopleCount(): Int

    @Query("select count(distinct people) from account where outIntype = 2")
    fun intPeopleCount(): Int

    @Query("SELECT sum(count) as count,outIntype, Event.name as eventName FROM Account, Relationship, Event ON Account.relationshipId =Relationship.id AND Account.eventId = Event.id group by outIntype, eventId order by eventName")
    fun eventChartList(): List<EventSum>

    @Query("SELECT sum(count) as count, outIntype, Relationship.name as relationshipName FROM Account, Relationship, Event ON Account.relationshipId =Relationship.id AND Account.eventId = Event.id group by outIntype, relationshipId order by relationshipName")
    fun relationshipChartList(): List<RelationshipSum>

    @Query("select count(*) from Account where state<0")
    fun checkUnUploadCount(): Int

    @Query("SELECT * FROM Account WHERE state<0 LIMIT 50")
    fun getAccountUploadList(): List<Account>

    @Update
    fun updateAccount(vararg accounts: Account)

    @Query("SELECT count(*) FROM Account WHERE eventId = :eventId")
    fun checkCountByEventId(eventId: Long): Int

    @Query("SELECT DISTINCT people from Account")
    fun getContactList(): List<String>
}