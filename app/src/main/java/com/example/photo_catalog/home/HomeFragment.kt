package com.example.photo_catalog.home

import android.graphics.BitmapFactory
import android.graphics.Path
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.photo_catalog.R
import com.example.photo_catalog.Timer.PhotoCatalogTimer
import com.example.photo_catalog.database.PhotoCatalogDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_home.view.*
import timber.log.Timber
import java.io.File
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import java.lang.Exception


class HomeFragment : Fragment() {

    private var testNumber = 0
    private val storageRef = Firebase.storage.reference
    private lateinit var timer: PhotoCatalogTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        timer = PhotoCatalogTimer(this.lifecycle)
        if (savedInstanceState != null) {
            testNumber = savedInstanceState.getInt("key_test")
        }
        Timber.i("onCreate called")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.fragment_home, container, false)

        try {
            val application = requireNotNull(this.activity).application

            val dataSource = PhotoCatalogDatabase.getInstance(application).photoCatalogDataDao

            val viewModelFactory = HomeViewModelFactory(dataSource, application)

            val homeViewModel = ViewModelProviders.of(
                this, viewModelFactory).get(HomeViewModel::class.java)

            homeViewModel.navigateToHome.observe(viewLifecycleOwner, Observer {
                item ->
                item?.let {
                    this.findNavController()
                }
            })
        }
        catch(ex: Exception){

        }
        downloadImages(view)

        return view
    }

    fun downloadImages(view: View) {
        view.bar_id.visibility = View.VISIBLE
        var islandRef = storageRef.child("pics/test")

        val localFile: File = File.createTempFile("images", "jpg")

        islandRef.getFile(localFile).addOnSuccessListener {
            val bmp = BitmapFactory.decodeFile(localFile.absolutePath)
            view.id_test.setImageBitmap(bmp)
            view.bar_id.visibility = View.GONE
        }.addOnFailureListener { exception ->
            Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
        }
    }


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

}