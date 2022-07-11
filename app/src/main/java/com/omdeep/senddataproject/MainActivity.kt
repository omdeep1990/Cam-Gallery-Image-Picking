package com.omdeep.senddataproject

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.ContextMenu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.omdeep.senddataproject.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private var binding : ActivityMainBinding? = null
    private var imageInBase64: String? = null
    private var selectedImage : Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        registerForContextMenu(binding!!.imageView)

        binding!!.sendData.setOnClickListener {
            val intent = Intent(this@MainActivity, ReceiveActivity::class.java)
            val bundle = Bundle()
            bundle.putString("first_Name", binding!!.firstNameEditText.text.toString())
            bundle.putString("last_Name", binding!!.lastNameEditText.text.toString())
            bundle.putString("employee_Id", binding!!.emplIdEditText.text.toString())
            bundle.putString("address", binding!!.addressEditText.text.toString())
            bundle.putString("image", imageInBase64)
            bundle.putString("imageUri", selectedImage.toString())

            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        val menuInflater : MenuInflater = getMenuInflater()
        menuInflater.inflate(R.menu.main_menu_bar, menu)
        super.onCreateContextMenu(menu, v, menuInfo)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            103 -> if (resultCode == RESULT_OK) {
                selectedImage = data?.data
                binding!!.imageView.setImageURI(selectedImage)
            }
            102 -> if (resultCode == RESULT_OK) {
                val bitmap = data!!.extras!!["data"] as Bitmap?
                imageInBase64 = bitmap?.let { Utility.convertBitmaptoBase64(it) }
                binding!!.imageView.setImageBitmap(bitmap)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // TODO: DO UR JOB
                cameraClickImage()
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.camera -> {
                askCameraPermission()
                true
            }
            R.id.gallery -> {
                askStoragePerssmission()
                true
            }
            R.id.cancel -> {
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
    private fun pickImageGallery() {
        val pickPhoto = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(pickPhoto, 103)
    }

    private fun cameraClickImage() {
        val camera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(camera, 102)
    }

    private fun askCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            cameraClickImage()
        } else {
            ActivityCompat.requestPermissions(
                this@MainActivity,
                arrayOf(Manifest.permission.CAMERA),
                1
            )
        }
    }

    private fun askStoragePerssmission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 104)
        } else {
            pickImageGallery()
        }
    }
}
