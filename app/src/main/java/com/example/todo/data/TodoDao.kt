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

    @Query("SELECT * FROM todo_table WHERE category_id = :id_cat AND status = 0 ORDER BY id DESC")
    fun getCatTodos(id_cat: Int): LiveData<List<Todo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: Category)

    @Query("INSERT INTO category_table (id, cat_title) VALUES (:id, :cat_title)")
    suspend fun insertFirstCategory(id: Int, cat_title: String);

    @Query("SELECT * FROM category_table")
    fun getAllCategory(): LiveData<List<Category>>

    @Query("DELETE FROM category_table WHERE id = :id_cat")
    suspend fun deleteCategory(id_cat: Int)

    @Query("DELETE FROM todo_table WHERE category_id = :id_cat")
    suspend fun deleteTodoCategory(id_cat: Int)

    @Query("SELECT * FROM category_table WHERE id = :id_cat")
    fun getCategoryId(id_cat: Int): LiveData<Category>

    @Query("SELECT COUNT(id) as count FROM category_table")
    fun isDataTrue(): LiveData<CountCategory>
}