package com.example.photo_catalog.AddItem

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.photo_catalog.R
import kotlinx.android.synthetic.main.fragment_add_item.view.*

class AddItemFragment : Fragment() {

    private lateinit var addItemViewModel: AddItemViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.i("AddItemFragment", "Called ViewModelProviders.of!")
        addItemViewModel = ViewModelProviders.of(this).get(AddItemViewModel::class.java)

        var view = inflater.inflate(R.layout.fragment_add_item, container, false)

        addItemViewModel.initialize(view)
        view.button_img2.setOnClickListener {
            var gallery: Intent = addItemViewModel.selectImg()
            this.startActivityForResult(Intent.createChooser(gallery, "Select Picture"), 1)
        }
        view.button_create2.setOnClickListener {
            addItemViewModel.onAdd(it)
        }

        addItemViewModel.eventAddedItem.observe(viewLifecycleOwner, Observer { isFinished ->
            if (isFinished) {
                addItemViewModel.onAdd(view)
            }
        })


        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            addItemViewModel.result(data)
            addItemViewModel.onAddedItemComplete()
        }
    }


}