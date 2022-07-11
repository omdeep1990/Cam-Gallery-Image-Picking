package com.omdeep.senddataproject

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.omdeep.senddataproject.databinding.ActivityReceiveBinding

class ReceiveActivity : AppCompatActivity() {
    private var binding: ActivityReceiveBinding? = null
    var imagevalue: String? = null
    var galleryImage : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReceiveBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

            val bundle = intent.extras

        binding!!.helloIntro.text = "Hello! = ${bundle!!.getString("first_Name")}" + " " + "${bundle!!.getString("last_Name")}" + " "+"Below are your card details"
            binding!!.firstName.text = "First Name = ${bundle!!.getString("first_Name")}"
            binding!!.lastName.text = "Last Name = ${bundle!!.getString("last_Name")}"
            binding!!.emplId.text = "Employee Id = ${bundle!!.getString("employee_Id")}"
            binding!!.address.text = "Address = ${bundle!!.getString("address")}"


        if (bundle!!.getString("image") != null) {

            imagevalue = bundle!!.getString("image")
            val bitmap = Utility.convertBase64ToBitmap(imagevalue)
            binding!!.imageView.setImageBitmap(bitmap)


        } else if (bundle!!.getString("imageUri") != null) {

            galleryImage = Uri.parse(bundle!!.getString("imageUri"))
            binding!!.imageView.setImageURI(galleryImage)

        } else {
            Toast.makeText(this, "Failed to send the image", Toast.LENGTH_SHORT).show()
        }
    }
}