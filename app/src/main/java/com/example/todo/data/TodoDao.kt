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

    @Query("UPDATE todo_table SET status = :status_num WHERE id = :id")
    suspend fun updateStatusTodo(status_num: Int, id: Int)

    @Query("SELECT * FROM todo_table WHERE status = :status_num ORDER BY id DESC")
    fun getAllTodos(status_num: Int): LiveData<List<Todo>>

    @Query("SELECT * FROM todo_table WHERE category_id = :id_cat ORDER BY id DESC")
    fun getCatTodos(id_cat: Int): LiveData<List<Todo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Query("SELECT * FROM category_table")
    fun getAllCategory(): LiveData<List<Category>>

    @Delete
    fun deleteCategory(category: Category)
}