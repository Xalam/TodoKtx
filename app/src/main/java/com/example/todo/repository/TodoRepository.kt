package com.example.todo.repository

import androidx.lifecycle.LiveData
import com.example.todo.data.Todo
import com.example.todo.data.TodoDao

class TodoRepository(private val todoDao: TodoDao) {

    val readAllData: LiveData<List<Todo>> = todoDao.getAllTodos()

    suspend fun addTodo(todo: Todo) {
        todoDao.insert(todo)
    }

    suspend fun updateTodo(todo: Todo) {
        todoDao.update(todo)
    }
}