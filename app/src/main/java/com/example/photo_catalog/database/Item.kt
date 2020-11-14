package com.example.photo_catalog.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_table")
data class Item(

    @PrimaryKey(autoGenerate = true)
    var itemId: Long = 0L,

    @ColumnInfo(name = "url_image")
    var uri: ByteArray? = null,

    @ColumnInfo(name = "label")
    var label: String = "",

    @ColumnInfo(name = "description")
    var description: String = ""
)