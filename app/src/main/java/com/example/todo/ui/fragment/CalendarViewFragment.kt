package com.example.todo.ui.fragment

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todo.data.Todo
import com.example.todo.databinding.FragmentCalendarViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import androidx.annotation.NonNull
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.lifecycle.Observer
import com.applandeo.materialcalendarview.EventDay
import com.example.todo.R
import com.example.todo.viewmodel.TodoViewModel

import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*
import kotlin.collections.ArrayList


class CalendarViewFragment: BottomSheetDialogFragment() {

    private lateinit var binding: FragmentCalendarViewBinding
    private lateinit var todoViewModel: TodoViewModel
    private var todoList = arrayListOf<Todo>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calendarView.setHeaderColor(R.color.blue_a_700)
        getAllTasks()
    }

    private fun getAllTasks() {
        //ViewModel
        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        todoViewModel.getAllData(0).observe(viewLifecycleOwner, Observer { t ->
            todoList = t as ArrayList<Todo>

            binding.calendarView.setEvents(getHighlightedDays())
        })
    }

    private fun getHighlightedDays(): List<EventDay> {
        val events = arrayListOf<EventDay>()

        for (i in todoList.indices) {
            val calendar = Calendar.getInstance()
            val items = todoList[i].date.split(" ").toTypedArray()
            val date = items[0].split("-").toTypedArray()
            val day = date[2]
            val month = date[1]
            val year = date[0]

            calendar[Calendar.DAY_OF_MONTH] = day.toInt()
            calendar[Calendar.MONTH] = month.toInt()
            calendar[Calendar.YEAR] = year.toInt()
            calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day))
            calendar.set(Calendar.MONTH, Integer.parseInt(month) - 1)
            calendar.set(Calendar.YEAR, Integer.parseInt(year))

            events.add(EventDay(calendar, R.drawable.ic_dot))
        }

        return events
    }
}