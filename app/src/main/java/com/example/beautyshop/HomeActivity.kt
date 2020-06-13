package com.example.beautyshop

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.annotation.NonNull
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.beautyshop.Model.Products
import com.example.beautyshop.Prevalent.Prevalent
import com.example.beautyshop.ViewHolder.ProductViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import io.paperdb.Paper


class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var ProductsRef: DatabaseReference? = null
    private var recyclerView: RecyclerView? = null
    private lateinit var layoutManager: RecyclerView.LayoutManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        ProductsRef = FirebaseDatabase.getInstance().reference.child("Products")


        Paper.init(this)


        val toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.setTitle("Home")
        setSupportActionBar(toolbar)


        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }


        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()


        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        val headerView = navigationView.getHeaderView(0)
        val userNameTextView = headerView.findViewById<TextView>(R.id.user_profile_name)
        val profileImageView = headerView.findViewById<ImageView>(R.id.user_profile_image)

        userNameTextView.text = Prevalent.currentOnlineUser.name
        Picasso.get().load(Prevalent.currentOnlineUser.getImage()).placeholder(R.drawable.profile).into(profileImageView)


        recyclerView = findViewById(R.id.recycler_menu)
        recyclerView!!.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        recyclerView!!.setLayoutManager(layoutManager)
    }


    override fun onStart() {
        super.onStart()

            val options = FirebaseRecyclerOptions.Builder<Products>()
                .setQuery(ProductsRef!!, Products::class.java)
                .build()


        val adapter = object : FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
            override fun onBindViewHolder(
                @NonNull holder: ProductViewHolder,
                position: Int,
                @NonNull model: Products
            ) {
                holder.txtProductName.setText(model.getPname())
                holder.txtProductDescription.setText(model.getDescription())
                holder.txtProductPrice.setText("Price = " + model.getPrice() + "$")
                Picasso.get().load(model.getImage()).into(holder.imageView)
            }

            @NonNull
            override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): ProductViewHolder {
                val view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_items_layout, parent, false)
                return ProductViewHolder(view)
            }
        }
        recyclerView!!.setAdapter(adapter)
        adapter.startListening()
    }

    override fun onBackPressed() {
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.getItemId()

        //        if (id == R.id.action_settings)
        //        {
        //            return true;
        //        }

        return super.onOptionsItemSelected(item)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.getItemId()

        if (id == R.id.nav_cart) {

        } else if (id == R.id.nav_orders) {

        } else if (id == R.id.nav_categories) {

        } else if (id == R.id.nav_settings) {
            val intent = Intent(this@HomeActivity, SettinsActivity::class.java)
            startActivity(intent)
        } else if (id == R.id.nav_logout) {
            Paper.book().destroy()

            val intent = Intent(this@HomeActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}


