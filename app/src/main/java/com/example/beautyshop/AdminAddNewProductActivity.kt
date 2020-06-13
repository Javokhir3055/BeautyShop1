@file:Suppress("DEPRECATION")

package com.example.beautyshop

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.*
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar
import java.util.HashMap


class AdminAddNewProductActivity : AppCompatActivity() {
    private var CategoryName: String? = null
    private var Description: String? = null
    private var Price: String? = null
    private var Pname: String? = null
    private var saveCurrentDate: String? = null
    private var saveCurrentTime: String? = null
    private var AddNewProductButton: Button? = null
    private var InputProductImage: ImageView? = null
    private var InputProductName: EditText? = null
    private var InputProductDescription: EditText? = null
    private var InputProductPrice: EditText? = null
    private var ImageUri: Uri? = null
    private var productRandomKey: String? = null
    private var downloadImageUrl: String? = null
    private var ProductImagesRef: StorageReference? = null
    private var ProductsRef: DatabaseReference? = null
    private var loadingBar: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_new_product)


        CategoryName = getIntent().getExtras()?.get("category").toString()
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images")
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products")


        AddNewProductButton = findViewById(R.id.add_new_product) as Button
        InputProductImage = findViewById(R.id.select_product_image) as ImageView
        InputProductName = findViewById(R.id.product_name) as EditText
        InputProductDescription = findViewById(R.id.product_description) as EditText
        InputProductPrice = findViewById(R.id.product_price) as EditText
        loadingBar = ProgressDialog(this)


        InputProductImage!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                OpenGallery()
            }
        })


        AddNewProductButton!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                ValidateProductData()
            }
        })
    }


    private fun OpenGallery() {
        val galleryIntent = Intent()
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT)
        galleryIntent.setType("image/*")
        startActivityForResult(galleryIntent, GalleryPick)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData()
            InputProductImage!!.setImageURI(ImageUri)
        }
    }


    private fun ValidateProductData() {
        Description = InputProductDescription!!.getText().toString()
        Price = InputProductPrice!!.getText().toString()
        Pname = InputProductName!!.getText().toString()


        if (ImageUri == null) {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Description)) {
            Toast.makeText(this, "Please write product description...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Price)) {
            Toast.makeText(this, "Please write product Price...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(Pname)) {
            Toast.makeText(this, "Please write product name...", Toast.LENGTH_SHORT).show()
        } else {
            StoreProductInformation()
        }
    }


    private fun StoreProductInformation() {
        loadingBar!!.setTitle("Add New Product")
        loadingBar!!.setMessage("Dear Admin, please wait while we are adding the new product.")
        loadingBar!!.setCanceledOnTouchOutside(false)
        loadingBar!!.show()

        val calendar = Calendar.getInstance()

        val currentDate = SimpleDateFormat("MMM dd, yyyy ")
        saveCurrentDate = currentDate.format(calendar.getTime())

        val currentTime = SimpleDateFormat("HH:mm:ss a")
        saveCurrentTime = currentTime.format(calendar.getTime())

        productRandomKey = saveCurrentDate!! + saveCurrentTime!!


        val filePath = ProductImagesRef!!.child(ImageUri!!.getLastPathSegment() + productRandomKey + ".jpg")

        val uploadTask = filePath.putFile(ImageUri!!)


        uploadTask.addOnFailureListener(object : OnFailureListener {
            override fun onFailure(@NonNull e: Exception) {
                val message = e.toString()
                Toast.makeText(this@AdminAddNewProductActivity, "Error: $message", Toast.LENGTH_SHORT).show()
                loadingBar!!.dismiss()
            }
        }).addOnSuccessListener(object : OnSuccessListener<UploadTask.TaskSnapshot> {
            override fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot) {
                Toast.makeText(this@AdminAddNewProductActivity, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show()

                val urlTask = uploadTask.continueWithTask(object : Continuation<UploadTask.TaskSnapshot, Task<Uri>> {
                    @Throws(Exception::class)
                    override fun then(@NonNull task: Task<UploadTask.TaskSnapshot>): Task<Uri> {
                        if (!task.isSuccessful()) {
                            throw task.getException()!!
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString()
                        return filePath.getDownloadUrl()
                    }
                }).addOnCompleteListener(object : OnCompleteListener<Uri> {
                    override fun onComplete(@NonNull task: Task<Uri>) {
                        if (task.isSuccessful()) {
                            downloadImageUrl = task.getResult().toString()

                            Toast.makeText(this@AdminAddNewProductActivity, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show()

                            SaveProductInfoToDatabase()
                        }
                    }
                })
            }
        })
    }


    private fun SaveProductInfoToDatabase() {
        val productMap = HashMap<String, Any>()
        productMap.put("pid", this!!.productRandomKey!!)
        productMap.put("date", this!!.saveCurrentDate!!)
        productMap.put("time", this!!.saveCurrentTime!!)
        productMap.put("description", this!!.Description!!)
        productMap.put("image", this!!.downloadImageUrl!!)
        productMap.put("category", this!!.CategoryName!!)
        productMap.put("price", this!!.Price!!)
        productMap.put("pname", this!!.Pname!!)

        ProductsRef!!.child(this!!.productRandomKey!!).updateChildren(productMap)
            .addOnCompleteListener(object : OnCompleteListener<Void> {
                override fun onComplete(@NonNull task: Task<Void>) {
                    if (task.isSuccessful()) {
                        val intent = Intent(this@AdminAddNewProductActivity, AdminCategoryActivity::class.java)
                        startActivity(intent)

                        loadingBar!!.dismiss()
                        Toast.makeText(this@AdminAddNewProductActivity, "Product is added successfully..", Toast.LENGTH_SHORT).show()
                    } else {
                        loadingBar!!.dismiss()
                        val message = task.getException().toString()
                        Toast.makeText(this@AdminAddNewProductActivity, "Error: $message", Toast.LENGTH_SHORT).show()
                    }
                }
            })
    }

    companion object {
        private val GalleryPick = 1
    }
}
