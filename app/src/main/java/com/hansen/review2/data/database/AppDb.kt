package com.hansen.review2.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hansen.review2.data.response.ItemsItem

@Database(entities = [ItemsItem::class], version = 1)
abstract class AppDb: RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDb? = null
        @JvmStatic
        fun getDatabase(context: Context): AppDb {
            if (INSTANCE == null) {
                synchronized(AppDb::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AppDb::class.java, "user_database")
                        .build()
                }
            }
            return INSTANCE as AppDb
        }
    }
}