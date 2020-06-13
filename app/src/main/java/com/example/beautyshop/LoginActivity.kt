@file:Suppress("DEPRECATION")

package com.example.beautyshop

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import com.example.beautyshop.Model.Users
import com.example.beautyshop.Prevalent.Prevalent
import com.google.firebase.database.*
import io.paperdb.Paper
import com.rey.material.widget.CheckBox

class LoginActivity : AppCompatActivity() {
    private var InputPhoneNumber: EditText? = null
    private var InputPassword: EditText? = null
    private var LoginButton: Button? = null
    private var loadingBar: ProgressDialog? = null
    private var AdminLink: TextView? = null
    private var NotAdminLink: TextView? = null

    private var parentDbName = "Users"
    private var chkBoxRememberMe: CheckBox? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        LoginButton = findViewById(R.id.login_btn) as Button
        InputPassword = findViewById(R.id.login_password_input) as EditText
        InputPhoneNumber = findViewById(R.id.login_phone_number_input) as EditText
        AdminLink = findViewById(R.id.admin_panel_link) as TextView
        NotAdminLink = findViewById(R.id.not_admin_panel_link) as TextView
        loadingBar = ProgressDialog(this)


        chkBoxRememberMe = findViewById(R.id.remember_me_chkb) as CheckBox
        Paper.init(this)


        LoginButton!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                LoginUser()
            }
        })

        AdminLink!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                LoginButton!!.setText("Login Admin")
                AdminLink!!.setVisibility(View.INVISIBLE)
                NotAdminLink!!.setVisibility(View.VISIBLE)
                parentDbName = "Admins"
            }
        })

        NotAdminLink!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                LoginButton!!.setText("Login")
                AdminLink!!.setVisibility(View.VISIBLE)
                NotAdminLink!!.setVisibility(View.INVISIBLE)
                parentDbName = "Users"
            }
        })
    }


    private fun LoginUser() {
        val phone = InputPhoneNumber!!.getText().toString()
        val password = InputPassword!!.getText().toString()

        if (TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please write your phone number...", Toast.LENGTH_SHORT).show()
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show()
        } else {
            loadingBar!!.setTitle("Login Account")
            loadingBar!!.setMessage("Please wait, while we are checking the credentials.")
            loadingBar!!.setCanceledOnTouchOutside(false)
            loadingBar!!.show()


            AllowAccessToAccount(phone, password)
        }
    }


    private fun AllowAccessToAccount(phone: String, password: String) {
        if (chkBoxRememberMe!!.isChecked()) {
            Paper.book().write(Prevalent.UserPhoneKey, phone)
            Paper.book().write(Prevalent.UserPasswordKey, password)
        }


        val RootRef: DatabaseReference
        RootRef = FirebaseDatabase.getInstance().getReference()


        RootRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(phone).exists()) {
                    val usersData = dataSnapshot.child(parentDbName).child(phone).getValue(Users::class.java)

                    if (usersData!!.getPhone().equals(phone)) {
                        if (usersData.getPassword().equals(password)) {
                            if (parentDbName == "Admins") {
                                Toast.makeText(this@LoginActivity, "Welcome Admin, you are logged in Successfully...", Toast.LENGTH_SHORT).show()
                                loadingBar!!.dismiss()

                                val intent = Intent(this@LoginActivity, AdminCategoryActivity::class.java)
                                startActivity(intent)
                            } else if (parentDbName == "Users") {
                                Toast.makeText(this@LoginActivity, "logged in Successfully...", Toast.LENGTH_SHORT).show()
                                loadingBar!!.dismiss()

                                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                                Prevalent.currentOnlineUser = usersData
                                startActivity(intent)
                            }
                        } else {
                            loadingBar!!.dismiss()
                            Toast.makeText(this@LoginActivity, "Password is incorrect.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Account with this $phone number do not exists.", Toast.LENGTH_SHORT).show()
                    loadingBar!!.dismiss()
                }
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {

            }
        })
    }
}