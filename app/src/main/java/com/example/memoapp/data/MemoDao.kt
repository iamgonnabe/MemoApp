package com.example.memoapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
abstract class MemoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addMemo(memoEntity: Memo)

    @Query("Select * from `memo-table`")
    abstract fun getAllMemos(): Flow<List<Memo>>

    @Query("Select * from `memo-table` where folder=:id")
    abstract fun getAllMemosByFolderId(id: Long): Flow<List<Memo>>

    @Update
    abstract suspend fun updateMemo(memoEntity: Memo)

    @Query("Select * from `memo-table` where id=:id")
    abstract fun getMemoById(id: Long): Flow<Memo>

    @Delete
    abstract fun deleteSelectedMemos(memoEntities: List<Memo>)

    @Delete
    abstract fun deleteMemo(memoEntity: Memo)

}

@Dao
abstract class FolderDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun addFolder(folderEntity: Folder)

    @Query("Select * from `folder`")
    abstract fun getAllFolders(): Flow<List<Folder>>

    @Update
    abstract suspend fun updateFolder(folderEntity: Folder)

    @Delete
    abstract suspend fun deleteFolder(folderEntity: Folder)

    @Query("SELECT * from `folder` where id=:id")
    abstract fun getFolderById(id: Long): Flow<Folder>
}