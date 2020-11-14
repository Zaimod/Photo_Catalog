package com.example.photo_catalog.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class PhotoCatalogDatabase : RoomDatabase() {

    abstract val photoCatalogDataDao: PhotoCatalogDatabaseDAO

    companion object {
        @Volatile
        private var INSTANCE: PhotoCatalogDatabase? = null

        fun getInstance(context: Context): PhotoCatalogDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PhotoCatalogDatabase::class.java,
                        "item_catalog_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}