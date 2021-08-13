package com.example.todo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Todo::class, Category::class], version = 2)
abstract class TodoDatabase: RoomDatabase() {

    abstract fun todoDao(): TodoDao

    companion object {
        private var instance: TodoDatabase? = null

        @Synchronized
        fun getInstance(context: Context): TodoDatabase {
            if (instance == null)
                instance = Room.databaseBuilder(context.applicationContext, TodoDatabase::class.java, "db_todo")
                    .build()

            return instance!!
        }
    }
}