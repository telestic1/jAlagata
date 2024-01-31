package com.example.jalagata.regester

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jalagata.MainActivity
import com.example.jalagata.R
import com.example.jalagata.SignInAndRegistrationActivity
import com.example.jalagata.databinding.ActivityWelcomeBinding
import com.google.firebase.auth.FirebaseAuth

class welcomeActivity : AppCompatActivity() {

   private  val binding:ActivityWelcomeBinding by lazy {
       ActivityWelcomeBinding.inflate(layoutInflater)
   }

    private  lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            val intent = Intent(Intent(this,SignInAndRegistrationActivity::class.java))
            intent.putExtra("action","login")
            startActivity(intent)
            finish()
        }

        binding.registerButton.setOnClickListener {
            val intent = Intent(Intent(this,SignInAndRegistrationActivity::class.java))
            intent.putExtra("action","register")
            startActivity(intent)
            finish()
        }
    }
    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}