package com.example.roomwordsample

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

// Class extends AndroidViewModel and requires application as a parameter.
class WordViewModel(application: Application) : AndroidViewModel(application) {
    private val LOG_TAG = "HEXAGON-LOG::" + object {}.javaClass.enclosingClass?.simpleName

    // The ViewModel maintains a reference to the repository to get data.
    private val repository: WordRepository
    private val nr: Int

    // LiveData gives us updated words when they change.
    val allWords: LiveData<List<Word>>

    val liveCounts: LiveData<Int>

    init {
        // Gets reference to WordDao from WordRoomDatabase to construct
        // the correct WordRepository.
        Log.d(LOG_TAG, "${object {}.javaClass.enclosingMethod?.name.toString()}: init ..")
        val wordsDao = WordRoomDatabase.getDatabase(application, viewModelScope).wordDao()
        repository = WordRepository(wordsDao)
        allWords = repository.allWords
        nr = 0
        liveCounts = repository.liveCounts
        Log.d(LOG_TAG, "${object {}.javaClass.enclosingMethod?.name.toString()}: Counts: ${liveCounts.value}")
    }

    /**
     * The implementation of insert() in the database is completely hidden from the UI.
     * Room ensures that you're not doing any long running operations on
     * the main thread, blocking the UI, so we don't need to handle changing Dispatchers.
     * ViewModels have a coroutine scope based on their lifecycle called
     * viewModelScope which we can use here.
     */
    fun insert(word: Word) = viewModelScope.launch {
        repository.insert(word)
    }

//    fun count() = viewModelScope.launch {
//        Log.d(LOG_TAG, "${object {}.javaClass.enclosingMethod?.name.toString()}: Start Counting")
//        val c=repository.count()
//        Log.d(LOG_TAG, "${object {}.javaClass.enclosingMethod?.name.toString()}: Count finished: $c")
//    }

    fun count(): Int  {
        Log.d(LOG_TAG, "${object {}.javaClass.enclosingMethod?.name.toString()}: Start Counting")
        val c=repository.count()
        Log.d(LOG_TAG, "${object {}.javaClass.enclosingMethod?.name.toString()}: Count finished: $c")
        return c
    }
}