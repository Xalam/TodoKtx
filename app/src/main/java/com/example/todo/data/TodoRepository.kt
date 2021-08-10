package com.example.todo.data

import androidx.lifecycle.LiveData

class TodoRepository(private val todoDao: TodoDao) {

    val readAllData: LiveData<List<Todo>> = todoDao.getAllTodos()

    suspend fun addTodo(todo: Todo) {
        todoDao.insert(todo)
    }
}