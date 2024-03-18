package com.example.memoapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "memo-table")
data class Memo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name="memo-title")
    val memo: String="",
    @ColumnInfo(name = "memo-time")
    val time: Date = Date(),
    @ColumnInfo(name = "folder")
    val folderId: Long = 0L
)

@Entity(tableName = "folder")
data class Folder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    @ColumnInfo(name = "folder-title")
    val title: String = ""
)

