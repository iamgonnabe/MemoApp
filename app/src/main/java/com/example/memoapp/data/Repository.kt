package com.example.memoapp.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class Repository(private val memoDao: MemoDao, private val folderDao: FolderDao) {
    suspend fun addMemo(memo: Memo){
        memoDao.addMemo(memo)
    }
    fun getMemos(): Flow<List<Memo>> = memoDao.getAllMemos()

    fun getMemoById(id:Long): Flow<Memo> = memoDao.getMemoById(id)

    fun getMemosByFolderId(folderId: Long): Flow<List<Memo>> = memoDao.getAllMemosByFolderId(folderId)

    suspend fun updateMemo(memo: Memo){
        memoDao.updateMemo(memo)
    }

    fun deleteSelectedMemos(memos: List<Memo>){
        memoDao.deleteSelectedMemos(memos)
    }
    fun deleteMemo(memo: Memo){
        memoDao.deleteMemo(memo)
    }

    suspend fun addFolder(folder: Folder){
        folderDao.addFolder(folder)
    }

    fun getFolders(): Flow<List<Folder>> = folderDao.getAllFolders()

    fun getFolderById(id: Long): Flow<Folder> = folderDao.getFolderById(id)

    suspend fun updateFolder(folder: Folder){
        folderDao.updateFolder(folder)
    }

    suspend fun deleteFolder(folder: Folder){
        folderDao.deleteFolder(folder)
    }

    suspend fun incrementMemoCount(folderId: Long) {
        val folder = folderDao.getFolderById(folderId).firstOrNull()
        folder?.let {
            it.memoCount++
            folderDao.updateFolder(it)
        }
    }

    suspend fun decrementMemoCount(folderId: Long, memoCount: Int) {
        val folder = folderDao.getFolderById(folderId).firstOrNull()
        folder?.let {
            it.memoCount -= memoCount
            folderDao.updateFolder(it)
        }
    }
}
