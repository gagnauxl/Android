package com.example.roomwordsample

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.roomwordsample.WordRoomDatabase.Companion.LOG_TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(Word::class), version = 1, exportSchema = false)
abstract class WordRoomDatabase : RoomDatabase() {


    abstract fun wordDao(): WordDao

    private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        private val LOG_TAG = "HEXAGON-LOG::" + object {}.javaClass.enclosingClass?.simpleName

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.wordDao())
                }
            }
        }

        suspend fun populateDatabase(wordDao: WordDao) {
            Log.d(LOG_TAG, "${object {}.javaClass.enclosingMethod?.name.toString()}: populate db")
            // Delete all content here.
            // wordDao.deleteAll()

            // Add sample words.
//            var word = Word("Hello")
//            wordDao.insert(word)
//            word = Word("World!")
//            wordDao.insert(word)

            // TODO: Add your own words!
        }
    }

    companion object {
        val LOG_TAG = "HEXAGON-LOG::" + object {}.javaClass.enclosingClass?.simpleName
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): WordRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                Log.d(LOG_TAG, "${object {}.javaClass.enclosingMethod?.name.toString()}: WordRoomDatabase.getDatabase: create singleton + add callback")
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "word_database"
                )
                    .addCallback(WordDatabaseCallback(scope))
                    .allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}