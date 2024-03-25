package com.example.memoapp

import android.content.Context
import androidx.room.Room
import com.example.memoapp.data.Folder
import com.example.memoapp.data.MemoDatabase
import com.example.memoapp.data.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object Graph {
    private lateinit var database: MemoDatabase

    val repository by lazy {
        Repository(memoDao = database.memoDao(), folderDao = database.folderDao())
    }

    fun provide(context: Context){
        val isFirstRun = context.getSharedPreferences("PREFS_NAME", Context.MODE_PRIVATE)
            .getBoolean("isFirstRun", true)

        if (isFirstRun) {
            database = Room.databaseBuilder(context, MemoDatabase::class.java, "memoApp.db").build()
            val folderDao = database.folderDao()
            CoroutineScope(Dispatchers.IO).launch {
                folderDao.addFolder(Folder(1L,"메모",0))
            }
            context.getSharedPreferences("PREFS_NAME", Context.MODE_PRIVATE)
                .edit()
                .putBoolean("isFirstRun", false)
                .apply()
        } else {
            database = Room.databaseBuilder(context, MemoDatabase::class.java, "memoApp.db").build()
        }
    }
}