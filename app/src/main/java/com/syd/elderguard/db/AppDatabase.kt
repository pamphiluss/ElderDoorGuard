package com.syd.elderguard.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.syd.elderguard.model.Account
import com.syd.elderguard.model.Event
import com.syd.elderguard.model.Relationship
import com.syd.elderguard.model.Todo

@Database(entities = [Relationship::class, Event::class, Account::class, Todo::class],
    version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun relationshipDao(): RelationshipDao

    abstract fun eventDao(): EventDao

    abstract fun accountDao(): AccountDao

    abstract fun todoDao(): TodoDao
}