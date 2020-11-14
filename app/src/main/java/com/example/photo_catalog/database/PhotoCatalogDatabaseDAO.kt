package com.example.photo_catalog.database

import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

interface PhotoCatalogDatabaseDAO {

    @Insert
    fun insert(item: Item)

    @Update
    fun update(item: Item)

    @Query("SELECT * FROM item_table WHERE itemId = :key")
    fun get(key: Long): Item

    @Query("DELETE FROM item_table")
    fun clean()

    @Query("SELECT * FROM item_table ORDER BY itemId DESC")
    fun getAllItems(): LiveData<List<Item>>

    @Query("SELECT * FROM item_table ORDER BY itemId DESC LIMIT 1")
    fun getToItem(): Item?
}