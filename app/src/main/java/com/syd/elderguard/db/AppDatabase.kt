package com.syd.elderguard.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.syd.elderguard.model.*

@Database(
    entities = [Relationship::class, Event::class, Account::class, Todo::class, Face::class],
    version = 2, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun relationshipDao(): RelationshipDao

    abstract fun eventDao(): EventDao

    abstract fun accountDao(): AccountDao

    abstract fun todoDao(): TodoDao

    abstract fun faceDao(): FaceDao
}