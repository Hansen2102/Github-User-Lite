package com.hansen.review2.data.database

import android.content.Context
import androidx.room.Room

class DbModule(context: Context) {
    private val database = Room.databaseBuilder(context, AppDb::class.java,"user_github_database")
        .allowMainThreadQueries()
        .build()
    val userDao = database.userDao()
}