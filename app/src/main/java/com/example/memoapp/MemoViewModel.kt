package com.example.memoapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.memoapp.data.Folder
import com.example.memoapp.data.Memo
import com.example.memoapp.data.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.Date

class MemoViewModel(private val repository: Repository = Graph.repository): ViewModel() {
    var memoState by mutableStateOf("")
    var memoTimeState by mutableStateOf(Date())
    var folderState by mutableStateOf("")
    var memosInFolder by mutableIntStateOf(0)
    var editFolderState by mutableStateOf(false)

    fun onMemoChanged(newString: String){
        memoState = newString
        memoTimeState = Date()
    }

    fun onFolderChanged(newString: String){
        folderState = newString
    }

    lateinit var getAllMemos: Flow<List<Memo>>
    lateinit var getAllFolders: Flow<List<Folder>>
    init {
        viewModelScope.launch {
            getAllMemos = repository.getMemos()
            getAllFolders = repository.getFolders()
        }
    }

    fun addMemo(memo: String, folderId : Long){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMemo(memo= Memo(folderId = folderId, memo = memo))
            repository.updateFolder(folder = Folder(id = folderId, memoCount = memosInFolder++))
        }
    }

    fun getMemoById(id:Long): Flow<Memo> {
        return repository.getMemoById(id)
    }

    fun updateMemo(memo: Memo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMemo(memo=memo)
        }
    }
    fun deleteMemo(memo: Memo, folderId : Long){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMemo(memo=memo)
            repository.updateFolder(folder = Folder(id = folderId, memoCount = memosInFolder--))
        }
    }

    fun deleteAllMemo(folderId: Long){
        memosInFolder = 0
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
            repository.updateFolder(folder = Folder(id = folderId, memoCount = memosInFolder))
        }
    }

    fun addFolder(folder: Folder){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addFolder(folder=folder)
        }
    }

    fun getFolderById(id: Long): Flow<Folder> {
        return repository.getFolderById(id)
    }

    fun updateFolder(folder: Folder){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateFolder(folder=folder)
        }
    }

    fun deleteFolder(folder: Folder){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFolder(folder=folder)
        }
    }
}