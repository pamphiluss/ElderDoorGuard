package com.syd.elderguard.db

import androidx.room.*
import com.syd.elderguard.model.Relationship
import com.syd.elderguard.model.RelationshipManager


@Dao
interface RelationshipDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertRelationship(relationship: Relationship)

    @Query("SELECT * FROM Relationship")
    fun getRelationshipList(): List<Relationship>

    @Insert
    fun insertRelationshipList(list: List<Relationship>)

    //关系管理
    @Query("SELECT Relationship.id as relationshipId, Relationship.name AS relationship ,COUNT(Relationship.name) relationshipCount, COUNT(distinct ACCOUNT.people) peopleCount FROM ACCOUNT, Relationship ON ACCOUNT.relationshipId = Relationship.id GROUP BY relationship ORDER BY relationshipCount DESC")
    fun getRelationshipManagerList(): List<RelationshipManager>

    @Query("DELETE FROM Relationship WHERE name = :name")
    fun deleteRelationship(name: String)

    @Query("select count(*) from RELATIONSHIP where state<0")
    fun checkUnUploadCount(): Int

    @Query("SELECT * FROM Relationship WHERE state<0 LIMIT 50")
    fun getRelationshipUploadList(): List<Relationship>

    @Update
    fun updateRelationship(vararg relationships: Relationship)
}