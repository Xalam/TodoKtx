package com.example.todo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.todo.data.Category
import com.example.todo.data.CountCategory
import com.example.todo.data.Todo
import com.example.todo.data.TodoDatabase
import com.example.todo.repository.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TodoViewModel(application: Application) : AndroidViewModel(application) {

//    val getAllData: LiveData<List<Todo>>
    val getAllCategory: LiveData<List<Category>>
    private val repository: TodoRepository

    init {
        val todoDao = TodoDatabase.getInstance(application).todoDao()
        repository = TodoRepository(todoDao)

//        getAllData = repository.readAllData
        getAllCategory = repository.readAllCategory
    }

    fun getAllData(status_num: Int): LiveData<List<Todo>> {
        return repository.readAllTodos(status_num)
    }

    fun addTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTodo(todo)
        }
    }

    fun updateTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTodo(todo)
        }
    }

    fun deleteTodo(todo: Todo) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTodo(todo)
        }
    }

    fun updateStatusTodo(status_num: Int, id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(500L)
            repository.updateStatusTodo(status_num, id)
        }
    }

    fun deleteAllTodos() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTodos()
        }
    }

    fun addCategoryTodo(category: Category) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addCategoryTodo(category)
        }
    }

    fun getTodoByCat(id_cat: Int): LiveData<List<Todo>> {
        return repository.readTodoByCat(id_cat)
    }

    fun deleteCategory(id_cat: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCategory(id_cat)
        }
    }

    fun deleteTodoCategory(id_cat: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTodoCategory(id_cat)
        }
    }

    fun getCategoryId(id_cat: Int): LiveData<Category> {
        return repository.getCategoryId(id_cat)
    }

    fun insertFirstCategory(id: Int, cat_title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertFirstCategory(id, cat_title)
        }
    }

    fun isDataTrue(): LiveData<CountCategory> {
        return repository.isDataTrue()
    }
}