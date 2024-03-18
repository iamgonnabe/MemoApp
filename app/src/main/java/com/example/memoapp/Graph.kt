package com.example.memoapp

import android.content.Context
import androidx.room.Room
import com.example.memoapp.data.MemoDatabase
import com.example.memoapp.data.Repository

object Graph {
    private lateinit var database: MemoDatabase

    val repository by lazy {
        Repository(memoDao = database.memoDao(), folderDao = database.folderDao())
    }

    fun provide(context: Context){
        database = Room.databaseBuilder(context, MemoDatabase::class.java, "memoApp.db").build()
    }
}