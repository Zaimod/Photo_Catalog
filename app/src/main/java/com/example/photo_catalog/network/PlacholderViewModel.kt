package com.example.photo_catalog.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PlaceholderViewModel: ViewModel() {

    private val _status = MutableLiveData<String>()
    val status: LiveData<String>
        get() = _status

    private val _property = MutableLiveData<PlaceholderProperty>()
    val property: LiveData<PlaceholderProperty>
        get() = _property

    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)


    init {
        getPlaceHolderProperties()
    }

    private fun getPlaceHolderProperties(){
        coroutineScope.launch {
            var getPropertiesDeferred = Api.retrofitService.getPropertiesAsync()
            try {
                val listResult = getPropertiesDeferred.await()
                if(listResult.isNotEmpty()){
                    _property.value = listResult[0]
                }
            }
            catch (t: Throwable){
                _status.value = "Failure: " + t.message
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}

