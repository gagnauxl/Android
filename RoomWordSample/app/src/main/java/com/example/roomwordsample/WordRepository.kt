package com.example.roomwordsample

import android.util.Log
import androidx.lifecycle.LiveData

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class WordRepository(private val wordDao: WordDao) {
    private val LOG_TAG = "HEXAGON-LOG::" + object {}.javaClass.enclosingClass?.simpleName

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allWords: LiveData<List<Word>> = wordDao.getAlphabetizedWords()

    // magic, when is it called
    var liveCounts = wordDao.liveCount()

    // The suspend modifier tells the compiler that this needs to be called
    // from a coroutine or another suspending function.
    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }

    fun count(): Int {
        var c: Int = wordDao.count()
        Log.d(LOG_TAG, "${object {}.javaClass.enclosingMethod?.name.toString()}: explicite count: $c")
        return c
    }
}