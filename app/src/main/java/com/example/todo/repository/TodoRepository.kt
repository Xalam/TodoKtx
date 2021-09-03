package com.example.todo.repository

import androidx.lifecycle.LiveData
import com.example.todo.data.Category
import com.example.todo.data.CountCategory
import com.example.todo.data.Todo
import com.example.todo.data.TodoDao

class TodoRepository(private val todoDao: TodoDao) {

//    val readAllData: LiveData<List<Todo>> = todoDao.getAllTodos()
    val readAllCategory: LiveData<List<Category>> = todoDao.getAllCategory()

    fun readAllTodos(status_num: Int): LiveData<List<Todo>> {
        return todoDao.getAllTodos(status_num)
    }

    suspend fun addTodo(todo: Todo) {
        todoDao.insert(todo)
    }

    suspend fun updateTodo(todo: Todo) {
        todoDao.update(todo)
    }

    suspend fun deleteTodo(todo: Todo) {
        todoDao.delete(todo)
    }

    suspend fun updateStatusTodo(status_num: Int, id: Int) {
        todoDao.updateStatusTodo(status_num, id)
    }

    suspend fun deleteAllTodos() {
        todoDao.deleteAllTodos()
    }

    suspend fun addCategoryTodo(category: Category) {
        todoDao.insertCategory(category)
    }

    fun readTodoByCat(id_cat: Int): LiveData<List<Todo>> {
        return todoDao.getCatTodos(id_cat)
    }

    suspend fun deleteCategory(id_cat: Int) {
        todoDao.deleteCategory(id_cat)
    }

    suspend fun deleteTodoCategory(id_cat: Int) {
        todoDao.deleteTodoCategory(id_cat)
    }

    fun getCategoryId(id_cat: Int): LiveData<Category> {
        return todoDao.getCategoryId(id_cat)
    }

    suspend fun insertFirstCategory(id: Int, cat_title: String) {
        return todoDao.insertFirstCategory(id, cat_title)
    }

    fun isDataTrue(): LiveData<CountCategory> {
        return todoDao.isDataTrue()
    }
}