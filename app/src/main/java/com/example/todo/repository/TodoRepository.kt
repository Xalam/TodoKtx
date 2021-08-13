package com.example.todo.repository

import androidx.lifecycle.LiveData
import com.example.todo.data.Category
import com.example.todo.data.Todo
import com.example.todo.data.TodoDao

class TodoRepository(private val todoDao: TodoDao) {

    val readAllData: LiveData<List<Todo>> = todoDao.getAllTodos()
    val readAllCategory: LiveData<List<Category>> = todoDao.getAllCategory()

    suspend fun addTodo(todo: Todo) {
        todoDao.insert(todo)
    }

    suspend fun updateTodo(todo: Todo) {
        todoDao.update(todo)
    }

    suspend fun deleteTodo(todo: Todo) {
        todoDao.delete(todo)
    }

    suspend fun deleteAllTodos() {
        todoDao.deleteAllTodos()
    }

    suspend fun addCategoryTodo(category: Category) {
        todoDao.insertCategory(category)
    }
}