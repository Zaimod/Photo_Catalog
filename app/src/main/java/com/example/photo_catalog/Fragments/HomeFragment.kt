package com.example.photo_catalog.Fragments

import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import com.example.photo_catalog.R
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_home.view.*
import java.io.File
//const val KEY_TEST = "key_test"

class HomeFragment : Fragment() {
    //private var testNumber = 0
    private val storageRef = Firebase.storage.reference
   // private lateinit var timer: PhotoCatalogTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //timer = PhotoCatalogTimer(this.lifecycle)
        //if(savedInstanceState != null){
        //    testNumber = savedInstanceState.getInt(KEY_TEST)
        //}
        //Timber.i("onCreate called")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_home, container, false)
        downloadImages(view)

        return view
    }
    private fun downloadImages(view: View) {
        view.bar_id.visibility = View.VISIBLE
        var islandRef = storageRef.child("pics/test")

        val localFile: File = File.createTempFile("images", "jpg")

        islandRef.getFile(localFile).addOnSuccessListener {
            val bmp = BitmapFactory.decodeFile(localFile.absolutePath)
            view.id_test.setImageBitmap(bmp)
            view.bar_id.visibility = View.GONE
        }.addOnFailureListener { exception ->
            Toast.makeText(activity, exception.message, Toast.LENGTH_SHORT).show()
        }
    }

    /*override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_TEST, testNumber)
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
     */
}