package com.example.memoapp.data

import kotlinx.coroutines.flow.Flow

class Repository(private val memoDao: MemoDao, private val folderDao: FolderDao) {
    suspend fun addMemo(memo: Memo){
        memoDao.addMemo(memo)
    }
    fun getMemos(): Flow<List<Memo>> = memoDao.getAllMemos()

    fun getMemoById(id:Long): Flow<Memo> = memoDao.getMemoById(id)

    suspend fun updateMemo(memo: Memo){
        memoDao.updateMemo(memo)
    }

    suspend fun deleteMemo(memo: Memo){
        memoDao.deleteMemo(memo)
    }

    fun deleteAll(){
        memoDao.deleteAll()
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
}
