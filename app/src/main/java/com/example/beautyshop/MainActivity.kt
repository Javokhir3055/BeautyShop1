package com.example.beautyshop

import android.app.ProgressDialog
import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.Toast

import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import androidx.annotation.NonNull
import com.example.beautyshop.Model.Users
import com.example.beautyshop.Prevalent.Prevalent
import com.google.firebase.database.*
import io.paperdb.Paper

class MainActivity : AppCompatActivity() {
    private var joinNowButton: Button? = null
    private var loginButton: Button? = null
    private var loadingBar: ProgressDialog? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        joinNowButton = findViewById(R.id.main_join_now_btn) as Button
        loginButton = findViewById(R.id.main_login_btn) as Button
        loadingBar = ProgressDialog(this)


        Paper.init(this)


        loginButton!!.setOnClickListener(object : View.OnClickListener {

            override fun onClick(view: View) {
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        })


        joinNowButton!!.setOnClickListener(object : View.OnClickListener {

            override fun onClick(view: View) {
                val intent = Intent(this@MainActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        })


        val UserPhoneKey = Paper.book().read<String>(Prevalent.UserPhoneKey)
        val UserPasswordKey = Paper.book().read<String>(Prevalent.UserPasswordKey)

        if (UserPhoneKey !== "" && UserPasswordKey !== "") {
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)) {
                AllowAccess(UserPhoneKey, UserPasswordKey)

                loadingBar!!.setTitle("Already Logged in")
                loadingBar!!.setMessage("Please wait.....")
                loadingBar!!.setCanceledOnTouchOutside(false)
                loadingBar!!.show()
            }
        }
    }


    private fun AllowAccess(phone: String, password: String) {
        val RootRef: DatabaseReference
        RootRef = FirebaseDatabase.getInstance().getReference()


        RootRef.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child("Users").child(phone).exists()) {
                    val usersData = dataSnapshot.child("Users").child(phone).getValue(Users::class.java)

                    if (usersData!!.getPhone().equals(phone)) {
                        if (usersData!!.getPassword().equals(password)) {
                            Toast.makeText(this@MainActivity, "Please wait, you are already logged in...", Toast.LENGTH_SHORT).show()
                            loadingBar!!.dismiss()

                            val intent = Intent(this@MainActivity, HomeActivity::class.java)
                            Prevalent.currentOnlineUser = usersData
                            startActivity(intent)
                        } else {
                            loadingBar!!.dismiss()
                            Toast.makeText(this@MainActivity, "Password is incorrect.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Account with this $phone number do not exists.", Toast.LENGTH_SHORT).show()
                    loadingBar!!.dismiss()
                }
            }


            override fun onCancelled(@NonNull databaseError: DatabaseError) {

            }
        })
    }
}