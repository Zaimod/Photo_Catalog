package com.example.photo_catalog.home

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.photo_catalog.AddItem.AddItemFragment
import com.example.photo_catalog.R
import com.example.photo_catalog.Timer.PhotoCatalogTimer
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.message_item.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import timber.log.Timber


private const val REQUEST_CODE_IMAGE_PICK = 0

class HomeFragment : Fragment() {

    var curFile: Uri? = null

    val imageRef = Firebase.storage.reference

    private var testNumber = 0
    private lateinit var timer: PhotoCatalogTimer
    private lateinit var recyclerview: RecyclerView

    lateinit var addItemFragment: AddItemFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        timer = PhotoCatalogTimer(this.lifecycle)
        if (savedInstanceState != null) {
            testNumber = savedInstanceState.getInt("key_test")
        }
        Timber.i("onCreate called")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerview = view.findViewById(R.id.recycler_id)
        recyclerview.layoutManager = LinearLayoutManager(context)
        listFiles()
        view.fab_btn.setOnClickListener {
            Toast.makeText(context, "Test button!", Toast.LENGTH_SHORT).show()
        }
        return view
    }



    private fun listFiles() = CoroutineScope(Dispatchers.IO).launch {
        try {
            val images = imageRef.child("pics/").listAll().await()
            val imageUrls = mutableListOf<String>()
            for(image in images.items) {
                val url = image.downloadUrl.await()
                imageUrls.add(url.toString())
            }
            withContext(Dispatchers.Main) {
                val imageAdapter = ImageAdapter(imageUrls)
                recycler_id.apply {
                    adapter = imageAdapter
                    layoutManager = LinearLayoutManager(context)
                }
            }
        } catch(e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_LONG).show()
            }
        }
    }



    /*
    fun downloadImages(view: View) {
        view.bar_id.visibility = View.VISIBLE
        var islandRef = storageRef.child("pics/test")

        val localFile: File = File.createTempFile("images", "jpg")

        islandRef.getFile(localFile).addOnSuccessListener {
            val bmp = BitmapFactory.decodeFile(localFile.absolutePath)
            //view.id_test.setImageBitmap(bmp)
            view.bar_id.visibility = View.GONE
        }.addOnFailureListener { exception ->
            Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
        }
    }
    */

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("key_test", testNumber)
        Timber.i("onSaveInstanceState called")
    }

    override fun onStart() {
        super.onStart()
        testNumber += 5
        Timber.i("onStart called")
        Timber.i("$testNumber")
    }

    override fun onResume() {
        super.onResume()
        Timber.i("onResume called")
    }

    override fun onPause() {
        super.onPause()
        Timber.i("onPause called")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy called")
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_IMAGE_PICK) {
            data?.data?.let {
                curFile = it
                imageView.setImageURI(it)
            }
        }
    }
}
