package com.example.jalagata

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.jalagata.Model.UserData
import com.example.jalagata.databinding.ActivitySignInAndRegistrationBinding
import com.example.jalagata.regester.welcomeActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SignInAndRegistrationActivity : AppCompatActivity() {
    private val binding: ActivitySignInAndRegistrationBinding by lazy {
        ActivitySignInAndRegistrationBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var storage: FirebaseStorage
    private val PICK_IMAGE_REQUEST = 1
    private var imageUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //Initialize firebase authentication
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()

        val action: String? = intent.getStringExtra("action")
        if (action == "login") {


            //visibility of fields
            binding.loginEmail.visibility = View.VISIBLE
            binding.loginPassword.visibility = View.VISIBLE
            binding.loginButton.visibility = View.VISIBLE

            //adjusting the visibility of fileds
            binding.registerButton.isEnabled = false
            binding.registerButton.alpha = 0.5f
            binding.registerNewHere.isEnabled = false
            binding.registerNewHere.alpha = 0.5f
            binding.registerEmail.visibility = View.GONE
            binding.registerPassword.visibility = View.GONE
            binding.cardView.visibility = View.GONE
            binding.registerName.visibility = View.GONE


            binding.loginButton.setOnClickListener {
                val loginEmail: String = binding.loginEmail.text.toString()
                val loginPassword: String = binding.loginPassword.text.toString()

                if (loginEmail.isEmpty() || loginPassword.isEmpty()){
                    Toast.makeText(this,"please fill the details 😒",Toast.LENGTH_SHORT).show()
                }else{
                    auth.signInWithEmailAndPassword(loginEmail,loginPassword)
                        .addOnCompleteListener {task->
                        if (task.isSuccessful){
                            Toast.makeText(this,"login Successful 👌❤️ "  ,Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this,MainActivity::class.java))
                            finish()
                        }else{
                            Toast.makeText(this,"login failed please enter correct detail 🤦‍♀️🤦",Toast.LENGTH_SHORT).show()
                        }

                        }
                }
            }

        } else if (action == "register") {
            binding.loginButton.isEnabled = false
            binding.loginButton.alpha = 0.5f

            binding.registerButton.setOnClickListener {
                //get data front edit text field
                val registerName: String = binding.registerName.text.toString()
                val registerEmail: String = binding.registerEmail.text.toString()
                val registerPassword: String = binding.registerPassword.text.toString()
                if (registerName.isEmpty() || registerEmail.isEmpty() || registerPassword.isEmpty()) {
                    Toast.makeText(this, "Please fill the  Details 😒", Toast.LENGTH_SHORT).show()
                } else {
                    auth.createUserWithEmailAndPassword(registerEmail, registerPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {


                                val user: FirebaseUser? = auth.currentUser
                                auth.signOut()
                                //Save user data in to Firebase realtime database
                                user?.let {
                                    val userReference: DatabaseReference =
                                        database.getReference("user")
                                    val userId: String = user.uid
                                    val userData = UserData(
                                        registerName,
                                        registerEmail
                                    )
                                    userReference.child(userId).setValue(userData)
                                        .addOnSuccessListener {
                                            Log.d("TAG","onCreate: data saved")
                                        }
                                        .addOnFailureListener {e->
                                            Log.e("TAG","Data save failed ${e.message}")
                                        }
                                    //upload image to firebase to storage
                                    val storageReference: StorageReference =
                                        storage.reference.child("profile_image/$userId.jpg")
                                    storageReference.putFile(imageUri!!).addOnCompleteListener{ task->
                                        storageReference.downloadUrl.addOnCompleteListener { imageUri->
                                            val imageUrl = imageUri.toString()

                                            // save image url  to the realtime database
                                            userReference.child(userId).child("profileImage").setValue(imageUrl)
                                            Glide.with(this)
                                                .load(imageUri)
                                                .apply(RequestOptions.circleCropTransform())
                                                .into(binding.registerUserImage)}
                                    }
                                    Toast.makeText(this, "User REGISTER Successful 😊❤️👣", Toast.LENGTH_SHORT).show()
                              startActivity(Intent(this,welcomeActivity::class.java))
                                    finish()
                                }
                            } else {
                                Toast.makeText(this, "User REGISTER Failed 🤷‍♀️🤷‍♂️", Toast.LENGTH_SHORT).show()
                            }

                        }
                }

            }
        }
        //set on clicklistner for the choose image
        binding.cardView.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "select image"),
                PICK_IMAGE_REQUEST
            )
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null)
            imageUri = data.data
        Glide.with(this)
            .load(imageUri)
            .apply(RequestOptions.circleCropTransform())
            .into(binding.registerUserImage)
    }


}