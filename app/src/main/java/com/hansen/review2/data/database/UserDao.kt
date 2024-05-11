package com.hansen.review2.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hansen.review2.data.response.ItemsItem

@Dao
interface UserDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insert(user: ItemsItem)

        @Query("SELECT * FROM User")
        fun loadAll(): LiveData<MutableList<ItemsItem>>

        @Query("SELECT * FROM User WHERE id LIKE :id LIMIT 1")
        fun findById(id: Int): ItemsItem

        @Delete
        fun delete(user: ItemsItem)
}