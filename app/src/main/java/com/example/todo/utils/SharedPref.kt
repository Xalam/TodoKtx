package com.example.todo.utils

import android.content.Context
import android.content.SharedPreferences

class SharedPref constructor(context: Context){

    private var sharedPreferences: SharedPreferences = context.getSharedPreferences(mainPref, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {
        val mainPref = "MAIN_PREF"
        val datePref = "datePref"
        val timePref = "timePref"
    }

    fun clearAll() {
        editor.clear()
        editor.commit()
    }

    fun setDate(date: String) {
        editor.putString(datePref, date)
        editor.commit()
    }

    fun getDate(): String {
        return sharedPreferences.getString(datePref, "").toString()
    }

    fun setTime(time: String) {
        editor.putString(timePref, time)
        editor.commit()
    }

    fun getTime(): String {
        return sharedPreferences.getString(timePref, "").toString()
    }
}