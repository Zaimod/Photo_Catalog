package com.example.photo_catalog

import androidx.room.Room
import org.junit.runner.RunWith
import androidx.test.runner.AndroidJUnit4
import com.example.photo_catalog.database.PhotoCatalogDatabase
import com.example.photo_catalog.database.PhotoCatalogDatabaseDAO
import org.junit.After
import org.junit.Before
import java.io.IOException
import kotlin.Throws
import androidx.test.platform.app.InstrumentationRegistry
import com.example.photo_catalog.database.Item
import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.Exception

@RunWith(AndroidJUnit4::class)
class PhotoCatalogDatabaseTest {

    private lateinit var photoCatalogDao: PhotoCatalogDatabaseDAO
    private lateinit var db: PhotoCatalogDatabase

    @Before
    fun createDb() {
        var context = InstrumentationRegistry.getInstrumentation().targetContext

        db = Room.inMemoryDatabaseBuilder(context, PhotoCatalogDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        photoCatalogDao = db.photoCatalogDataDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetItem(){
        val item = Item()
        photoCatalogDao.insert(item)
        val toitem = photoCatalogDao.getToItem()
        assertEquals(toitem?.label, "test")
    }
}