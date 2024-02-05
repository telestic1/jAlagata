package com.example.jalagata

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.jalagata.Model.JalagataItemModel
import com.example.jalagata.adapter.JalagataAdapter
import com.example.jalagata.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue

class MainActivity : AppCompatActivity() {
    private  val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private  lateinit var  databaseReference: DatabaseReference
    private  val jalagataItems = mutableSetOf<JalagataItemModel>()
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
//to go save article page
        binding.saveArticleBtn.setOnClickListener {
            startActivity(Intent(this,SavedArticleActivity::class.java))
        }
      //to go to progile activity
binding.profileImage.setOnClickListener {
    startActivity(Intent(this ,profileActivity::class.java))
}
        //to go to progile activity
        binding.cardView2.setOnClickListener {
            startActivity(Intent(this ,profileActivity::class.java))
        }


        auth = FirebaseAuth.getInstance()
        databaseReference =FirebaseDatabase.getInstance() .getReference().child("blogs")

        val userId = auth.currentUser?.uid

        //set user profile

        if (userId !=null){
            loadUserProfileImage(userId)
        }

            //set jAlagata post in Recycler view

            //Initilize the recycler view  and set adapter

        val recyclerView = binding.jalagaataRecyclerView
        val jalagataAdapter = JalagataAdapter(jalagataItems)
        recyclerView.adapter = jalagataAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

  //fetch data from data base
  databaseReference.addValueEventListener(object :ValueEventListener {
      override fun onDataChange(snapshot: DataSnapshot) {
          jalagataItems.clear()
          for (snapshot in snapshot.children)
          val jalagataItem = snapshot.getValue(JalagataItemModel::class.java)
          if (jalagataItem != null) {
              jalagataItem.add(jalagataItem)
          }

          // reverse the list
          jalagataItems.reversed()
          //notify the Adapter the data has changed
          jalagataAdapter.notifyDataSetChanged()
      }

      override fun onCancelled(error: DatabaseError) {
         Toast.makeText(this@MainActivity,"jAlagata loading  failed",Toast.LENGTH_SHORT).show()
      }
  })
               binding.floatingActionButton.setOnClickListener {
              startActivity(Intent(this,AddArticleActivity::class.java))
      }
    }

    private fun loadUserProfileImage(userId: String) {
       val userReference = FirebaseDatabase.getInstance().reference.child("users").child(userId)
        userReference.child("profileImage").addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
             val profileImageUrl = snapshot.getValue(String ::class.java)

                if (profileImageUrl !=null){
                    Glide.with(this@MainActivity)
                        .load(profileImageUrl)
                        .into(binding.profileImage)
                }
            }

               override fun onCancelled(error: DatabaseError) {
                 Toast.makeText(this@MainActivity,"Error loading image 😢", Toast.LENGTH_SHORT).show()
            }

        } )
    }
}