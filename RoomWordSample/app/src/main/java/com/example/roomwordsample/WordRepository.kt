package com.example.roomwordsample

import androidx.lifecycle.LiveData

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class WordRepository(private val wordDao: WordDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allWords: LiveData<List<Word>> = wordDao.getAlphabetizedWords()

    // The suspend modifier tells the compiler that this needs to be called
    // from a coroutine or another suspending function.
    suspend fun insert(word: Word) {
        wordDao.insert(word)
    }
}