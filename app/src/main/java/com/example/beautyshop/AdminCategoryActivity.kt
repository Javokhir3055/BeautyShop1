package com.example.beautyshop

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView

class AdminCategoryActivity : AppCompatActivity() {
    private var tShirts: ImageView? = null
    private var sportsTShirts: ImageView? = null
    private var femaleDresses: ImageView? = null
    private var sweathers: ImageView? = null
    private var glasses: ImageView? = null
    private var hatsCaps: ImageView? = null
    private var walletsBagsPurses: ImageView? = null
    private var shoes: ImageView? = null
    private var headPhonesHandFree: ImageView? = null
    private var Laptops: ImageView? = null
    private var watches: ImageView? = null
    private var mobilePhones: ImageView? = null


     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_category)


        tShirts = findViewById(R.id.t_shirts) as ImageView
        sportsTShirts = findViewById(R.id.sports_t_shirts) as ImageView
        femaleDresses = findViewById(R.id.female_dresses) as ImageView
        sweathers = findViewById(R.id.sweathers) as ImageView

        glasses = findViewById(R.id.glasses) as ImageView
        hatsCaps = findViewById(R.id.hats_caps) as ImageView
        walletsBagsPurses = findViewById(R.id.purses_bags_wallets) as ImageView
        shoes = findViewById(R.id.shoes) as ImageView

        headPhonesHandFree = findViewById(R.id.headphoness_handfree) as ImageView
        Laptops = findViewById(R.id.laptop_pc) as ImageView
        watches = findViewById(R.id.watches) as ImageView
        mobilePhones = findViewById(R.id.mobilephones) as ImageView


        tShirts!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val intent = Intent(this@AdminCategoryActivity, AdminAddNewProductActivity::class.java)
                intent.putExtra("category", "tShirts")
                startActivity(intent)
            }
        })


        sportsTShirts!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val intent = Intent(this@AdminCategoryActivity, AdminAddNewProductActivity::class.java)
                intent.putExtra("category", "Sports tShirts")
                startActivity(intent)
            }
        })


        femaleDresses!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val intent = Intent(this@AdminCategoryActivity, AdminAddNewProductActivity::class.java)
                intent.putExtra("category", "Female Dresses")
                startActivity(intent)
            }
        })


        sweathers!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val intent = Intent(this@AdminCategoryActivity, AdminAddNewProductActivity::class.java)
                intent.putExtra("category", "Sweathers")
                startActivity(intent)
            }
        })


        glasses!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val intent = Intent(this@AdminCategoryActivity, AdminAddNewProductActivity::class.java)
                intent.putExtra("category", "Glasses")
                startActivity(intent)
            }
        })


        hatsCaps!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val intent = Intent(this@AdminCategoryActivity, AdminAddNewProductActivity::class.java)
                intent.putExtra("category", "Hats Caps")
                startActivity(intent)
            }
        })



        walletsBagsPurses!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val intent = Intent(this@AdminCategoryActivity, AdminAddNewProductActivity::class.java)
                intent.putExtra("category", "Wallets Bags Purses")
                startActivity(intent)
            }
        })


        shoes!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val intent = Intent(this@AdminCategoryActivity, AdminAddNewProductActivity::class.java)
                intent.putExtra("category", "Shoes")
                startActivity(intent)
            }
        })



        headPhonesHandFree!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val intent = Intent(this@AdminCategoryActivity, AdminAddNewProductActivity::class.java)
                intent.putExtra("category", "HeadPhones HandFree")
                startActivity(intent)
            }
        })


        Laptops!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val intent = Intent(this@AdminCategoryActivity, AdminAddNewProductActivity::class.java)
                intent.putExtra("category", "Laptops")
                startActivity(intent)
            }
        })


        watches!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val intent = Intent(this@AdminCategoryActivity, AdminAddNewProductActivity::class.java)
                intent.putExtra("category", "Watches")
                startActivity(intent)
            }
        })


        mobilePhones!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val intent = Intent(this@AdminCategoryActivity, AdminAddNewProductActivity::class.java)
                intent.putExtra("category", "Mobile Phones")
                startActivity(intent)
            }
        })
    }
}