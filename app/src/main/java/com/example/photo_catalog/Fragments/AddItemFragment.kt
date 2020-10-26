package com.example.photo_catalog.Fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.photo_catalog.MainActivity
import com.example.photo_catalog.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_add_item.*
import kotlinx.android.synthetic.main.fragment_add_item.view.*
import java.io.ByteArrayOutputStream

class AddItemFragment : Fragment() {

    lateinit var imageUri: Uri
    lateinit var image_item: ImageView
    lateinit var edit_label: EditText
    lateinit var edit_desk: EditText
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_add_item, container, false)

        image_item =  view.findViewById(R.id.button_img2)
        edit_label = view.findViewById(R.id.label_id2)
        edit_desk = view.findViewById(R.id.desc_id2)
        progressBar = view.findViewById(R.id.progressBar2)

        view.button_img2.setOnClickListener {
            var gallery: Intent = Intent()
            gallery.type = "image/*"
            gallery.action = Intent.ACTION_GET_CONTENT

            this.startActivityForResult(Intent.createChooser(gallery, "Select Picture"), 1)
        }
        view.button_create2.setOnClickListener {
            if (edit_label.text.toString() != "" && edit_desk.text.toString() != "") {
                Snackbar.make(it, "Congratulations!", Snackbar.LENGTH_SHORT)
                    .show()
            }
        }
        return view
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == 1) {
            image_item.setImageURI(data?.data)
            val bitmap = (image_item.drawable as BitmapDrawable).bitmap
            uploadImageAndSaveUri(bitmap)
        }
    }
    private  fun uploadImageAndSaveUri(bitmap: Bitmap){
        progressBar.visibility = View.VISIBLE
        val baos = ByteArrayOutputStream()
        val storageRef = FirebaseStorage.getInstance()
            .reference
            .child("pics/test")
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()

        val upload = storageRef.putBytes(image)

        upload.addOnCompleteListener{uploadTask ->
            if(uploadTask.isSuccessful){
                storageRef.downloadUrl.addOnCompleteListener {urlTask ->
                    urlTask.result?.let{
                        imageUri = it
                        image_item.setImageBitmap(bitmap)
                    }
                }
            } else{
                uploadTask.exception?.let {
                    Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                }
            }
            progressBar.visibility = View.GONE
        }
    }
}