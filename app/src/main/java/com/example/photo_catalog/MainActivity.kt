package com.example.photo_catalog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.FragmentTransaction
import com.example.photo_catalog.Fragments.AboutFragment
import com.example.photo_catalog.Fragments.AddItemFragment
import com.example.photo_catalog.Fragments.HomeFragment
import com.example.photo_catalog.Timer.ActiveTimer
import com.example.photo_catalog.Timer.PhotoCatalogTimer
import com.example.photo_catalog.Timer.ToDestroyTimer
import com.google.android.material.navigation.NavigationView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import timber.log.Timber

//const val KEY_TEST = "key_test"

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val storageRef = Firebase.storage.reference
    lateinit var homeFragment: HomeFragment
    lateinit var aboutFragment: AboutFragment
    lateinit var addItemFragment: AddItemFragment
    private lateinit var timerActive: PhotoCatalogTimer
    private lateinit var timerToDestroy: PhotoCatalogTimer
    private var testNumber = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        val actionBar = supportActionBar
        actionBar?.title = "Photo"

        val drawerToggle: ActionBarDrawerToggle = object : ActionBarDrawerToggle(
            this,
            drawer_Layout,
            toolbar,
            (R.string.open),
            (R.string.close)
        ) { }

        drawerToggle.isDrawerIndicatorEnabled = true
        drawer_Layout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        homeFragment = HomeFragment()
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_layout, homeFragment)
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .commit()


        timerActive = ActiveTimer(this.lifecycle)
        timerToDestroy = ToDestroyTimer(this.lifecycle)

        if(savedInstanceState != null){
            testNumber = savedInstanceState.getInt("key_test")
        }
        Timber.i("onCreate called")
        //fab_new_item.setOnClickListener {
        //    startActivity(Intent(this, AddNewItemActivity::class.java))
        //}
        //downloadImages()
    }

    /*private fun downloadImages() {
        bar_id.visibility = View.VISIBLE
        var islandRef = storageRef.child("pics/test")

        val localFile: File = File.createTempFile("images", "jpg")


        islandRef.getFile(localFile).addOnSuccessListener {
            // Data for "images/island.jpg" is returned, use this as needed
            val bmp = BitmapFactory.decodeFile(localFile.absolutePath)
            image_id.setImageBitmap(bmp)
            bar_id.visibility = View.GONE
        }.addOnFailureListener { exception ->
            Toast.makeText(this@MainActivity, exception.message, Toast.LENGTH_LONG).show()
        }

    }*/

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        val actionBar = supportActionBar
        when(menuItem.itemId){
            R.id.home -> {
                homeFragment = HomeFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, homeFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
                actionBar?.title = "Photo"
            }
            R.id.addItem -> {
                addItemFragment = AddItemFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, addItemFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
                actionBar?.title = "Add Item"
            }
            R.id.about -> {
                aboutFragment = AboutFragment()
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.frame_layout, aboutFragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit()
                actionBar?.title = "About"
            }
        }
        drawer_Layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if(drawer_Layout.isDrawerOpen(GravityCompat.START)){
            drawer_Layout.closeDrawer(GravityCompat.START)
        }
        else{
            super.onBackPressed()
        }
    }
    //при повороті екрану і натисканні кнопки Home
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt("key_test", testNumber++)
        Timber.i("onSaveInstanceState called")

    }

    override fun onStart() {
        super.onStart()
        Timber.i("onStart called")
        Timber.i("testNumber = $testNumber")
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
        var percent = timerActive.secondsCount.toDouble()/timerToDestroy.secondsCount.toDouble() * 100
        Timber.i("Time from onStart to onDestroy is : ${timerActive.secondsCount}/${timerToDestroy.secondsCount} seconds : $percent %")
    }
}
