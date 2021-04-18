package com.syd.elderguard.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.syd.elderguard.model.Todo

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTodo(todo: Todo)

    @Query("SELECT * FROM Todo ORDER BY dateTime ASC")
    fun getTodoList(): List<Todo>

    @Query("DELETE FROM Todo WHERE id = :id")
    fun deleteTodoById(id: Long)
}