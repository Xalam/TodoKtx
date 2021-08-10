package com.example.todo.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.data.Todo
import com.example.todo.databinding.ItemTodoBinding

class TodoAdapter: RecyclerView.Adapter<TodoAdapter.ListViewHolder>() {

    private var todoList = emptyList<Todo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoAdapter.ListViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TodoAdapter.ListViewHolder, position: Int) {
        val currentItem = todoList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return todoList.size
    }

    inner class ListViewHolder(private val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: Todo) {
            with(binding) {
                textTitleItem.text = todo.title
                textDescriptionItem.text = todo.description
            }
        }
    }

    fun setData(todo: List<Todo>) {
        this.todoList = todo
        notifyDataSetChanged()
    }
}