package com.example.todo.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.data.Todo
import com.example.todo.databinding.ItemTodoBinding
import com.example.todo.ui.fragment.HomeFragmentDirections
import com.example.todo.utils.ItemClick
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class TodoAdapter : RecyclerView.Adapter<TodoAdapter.ListViewHolder>() {

    private var todoList = emptyList<Todo>()
    private lateinit var itemClick: ItemClick
    private var dateFormat = SimpleDateFormat("EE dd MMM yyyy", Locale.US)
    private var inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private var timeFormat = SimpleDateFormat("HH:mm", Locale.US)
    private var inputTimeFormat = SimpleDateFormat("hh:mm a", Locale.US)
    private var date: Date? = null
    private var time: Date? = null

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

    inner class ListViewHolder(private val binding: ItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(todo: Todo) {
            with(binding) {
                textTitleItem.text = todo.title
                textDescriptionItem.text = todo.description

                itemTodoLayout.setOnClickListener {
                    val action = HomeFragmentDirections.actionHomeFragmentToEditFragment(todo)
                    Navigation.findNavController(itemTodoLayout).navigate(action)
                }

                try {
                    val dateDatabase = todo.date
                    val dateDatabaseArray  = arrayOf(dateDatabase.split(" "))
                    val dateString = dateDatabaseArray[0][0]
                    val timeString = dateDatabaseArray[0][1]

                    date = inputDateFormat.parse(dateString)
                    val outputDateString = dateFormat.format(date)

                    val itemsDate = outputDateString.split(" ")

                    dayItem.text = itemsDate[0]
                    dateItem.text = itemsDate[1]
                    monthItem.text = itemsDate[2]

                    time = timeFormat.parse(timeString)
                    val outputTimeString = inputTimeFormat.format(time)

                    textTimeItem.text = outputTimeString

                } catch (e: Exception) {
                    e.printStackTrace()
                }

                if (todo.status == 1) {
                    checkItem.progress = 1f
                } else {
                    checkItem.progress = 0f
                }

                checkItem.setOnClickListener {
                    if (checkItem.progress == 0f) {
                        checkItem.speed = 2.5F
                        checkItem.playAnimation()
                        itemClick.onItemClickStatus(todo.id!!, todo.status)
                    } else {
                        checkItem.progress = 0f
                        itemClick.onItemClickStatus(todo.id!!, todo.status)
                    }
                }
            }
        }
    }

    fun setData(todo: List<Todo>) {
        this.todoList = todo
        notifyDataSetChanged()
    }

    fun setItemClick(item: ItemClick) {
        this.itemClick = item
    }
}