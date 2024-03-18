package com.example.memoapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Memo::class, Folder::class],
    version = 1,
    exportSchema = false
)
abstract class MemoDatabase: RoomDatabase() {
    abstract fun memoDao(): MemoDao
    abstract fun folderDao(): FolderDao
}

