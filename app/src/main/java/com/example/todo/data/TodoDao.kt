package com.example.todo.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: Todo)

    @Update
    suspend fun update(todo: Todo)

    @Delete
    suspend fun delete(todo: Todo)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAllTodos()

    @Query("Select * From todo_table order by id desc")
    fun getAllTodos(): LiveData<List<Todo>>

    @Query("Select * From todo_table where category_id = :id_cat order by id desc")
    fun getCatTodos(id_cat: Int): LiveData<List<Todo>>

    @Insert
    fun insertCategory(category: Category)

    @Delete
    fun deleteCategory(category: Category)
}