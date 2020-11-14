package com.example.photo_catalog.AddItem

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.photo_catalog.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class AddItemViewModel : ViewModel() {

    lateinit var imageUri: Uri
    lateinit var imageItem: ImageView

    lateinit var editLabel: EditText
    lateinit var editDesk: EditText
    lateinit var progressBar: ProgressBar

    private val _eventAddedItem = MutableLiveData<Boolean>()
    val eventAddedItem: LiveData<Boolean>
        get() = _eventAddedItem

    init {
        _eventAddedItem.value = false
        Log.i("AddItemViewModel", "AddItemViewModel created!")
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("AddItemViewModel", "AddItemViewModel destroyed!")
    }

    private fun uploadImageAndSaveUri(bitmap: Bitmap) {
        progressBar.visibility = View.VISIBLE
        val baos = ByteArrayOutputStream()
        val storageRef = FirebaseStorage.getInstance()
            .reference
            .child("pics/test")
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()

        val upload = storageRef.putBytes(image)

        upload.addOnCompleteListener { uploadTask ->
            if (uploadTask.isSuccessful) {
                storageRef.downloadUrl.addOnCompleteListener { urlTask ->
                    urlTask.result?.let {
                        imageUri = it
                        imageItem.setImageBitmap(bitmap)
                    }
                }
                _eventAddedItem.value = true
            }
            progressBar.visibility = View.GONE
        }
    }

    fun selectImg(): Intent {
        var gallery: Intent = Intent()
        gallery.type = "image/*"
        gallery.action = Intent.ACTION_GET_CONTENT

        return gallery
    }

    fun onAdd(it: View) {
        if (editLabel.text.toString() != "" && editDesk.text.toString() != "") {
            Snackbar.make(it, "Congratulations!", Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    fun result(data: Intent?) {
        imageItem.setImageURI(data?.data)
        val bitmap = (imageItem.drawable as BitmapDrawable).bitmap
        uploadImageAndSaveUri(bitmap)
    }

    fun initialize(view: View) {
        imageItem = view.findViewById(R.id.button_img2)
        editLabel = view.findViewById(R.id.label_id2)
        editDesk = view.findViewById(R.id.desc_id2)
        progressBar = view.findViewById(R.id.progressBar2)
    }

    fun onAddedItemComplete() {
        _eventAddedItem.value = false
    }
}