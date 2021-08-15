package com.example.todo.utils

import java.util.*

class OtherFunction {
    companion object {
        fun getTodayDate(): Int {
            val calendar: Calendar = Calendar.getInstance()
            var month: Int = calendar.get(Calendar.MONTH)
            val day: Int = calendar.get(Calendar.DAY_OF_MONTH)

            return day
        }
    }
}