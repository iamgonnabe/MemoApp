package com.example.memoapp

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MemoViewModel(private val repository: Repository = Graph.repository): ViewModel() {
    var memoState by mutableStateOf("")
    var memoTimeState by mutableStateOf(Date())
    var folderState by mutableStateOf("")
    var memosInFolder by mutableIntStateOf(0)
    var editFolderState by mutableStateOf(false)
    var memoCreatingState by mutableStateOf(false)
    var memoEditingState by mutableStateOf(false)
    var memoSelectedListState = mutableStateListOf<Boolean>()
    var deleteMemoList = mutableStateListOf<Memo>()

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
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("yyyy.MM.dd. HH:mm", Locale.KOREAN)
        val dateString = dateFormat.format(currentDate)
        viewModelScope.launch(Dispatchers.IO) {
            repository.addMemo(memo= Memo(folderId = folderId, memo = memo, time = dateString))
        }
        Log.d("folderId","$folderId")
    }

    fun getMemoById(id:Long): Flow<Memo> {
        return repository.getMemoById(id)
    }

    fun getAllMemosByFolderId(folderId: Long): Flow<List<Memo>> {
        return repository.getMemosByFolderId(folderId)
    }

    fun updateMemo(memo: Memo){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateMemo(memo=memo)
        }
    }

    fun deleteMemos(memos: List<Memo>){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteMemos(memos)
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

    fun incrementMemoCount(folderId: Long) {
        viewModelScope.launch {
            repository.incrementMemoCount(folderId)
        }
    }

    fun decrementMemoCount(folderId: Long, memoCount: Int) {
        viewModelScope.launch {
            repository.decrementMemoCount(folderId, memoCount)
        }
    }

    fun deleteFolder(folder: Folder){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFolder(folder=folder)
        }
    }
}