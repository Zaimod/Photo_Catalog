package com.example.photo_catalog.home

import android.app.Application
import android.provider.SyncStateContract.Helpers.insert
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.photo_catalog.database.Item
import com.example.photo_catalog.database.PhotoCatalogDatabaseDAO
import kotlinx.coroutines.*

class HomeViewModel(
    val database: PhotoCatalogDatabaseDAO,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val toitem = MutableLiveData<Item?>()

    private val items = database.getAllItems()

    private val _navigateToHome = MutableLiveData<Item>()
    val navigateToHome: LiveData<Item>
        get() = _navigateToHome

    fun doneNavigation(){
        _navigateToHome.value = null
    }

    init {
        initializeToItem()
    }


    private fun initializeToItem() {
        uiScope.launch {
            toitem.value = getToItemFromDatabase()
        }
    }

    private suspend fun getToItemFromDatabase(): Item? {
        return withContext(Dispatchers.IO) {
            var item = database.getToItem()
            item
        }
    }

    fun onStartTracking() {
        uiScope.launch {
            var newItem = Item()
            insert(newItem)
            toitem.value = getToItemFromDatabase()
        }
    }

    private suspend fun insert(item: Item) {
        withContext(Dispatchers.IO) {
            database.insert(item)
        }
    }

    fun onStopTracking() {
        uiScope.launch {
            val oldItem = toitem.value ?: return@launch
            update(oldItem)
             _navigateToHome.value = oldItem
        }
    }

    private suspend fun update(item: Item) {
        withContext(Dispatchers.IO) {
            database.update(item)
        }
    }
    fun onClear(){
        uiScope.launch {
            clear()
            toitem.value = null
        }
    }
    suspend fun clear(){
        withContext(Dispatchers.IO){
            database.clean()
        }
    }
}