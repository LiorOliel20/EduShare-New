package com.example.edushare_new.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.edushare_new.data.local.model.Post

@Database(entities = [Post::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "edushare.db"
                )
                    .fallbackToDestructiveMigration()  // ✅ מוסיפים שורה זו
                    .build().also { INSTANCE = it }
            }
    }
}

