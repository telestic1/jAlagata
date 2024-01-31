package com.example.jalagata.regester

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jalagata.R
import com.example.jalagata.SignInAndRegistrationActivity
import com.example.jalagata.databinding.ActivityWelcomeBinding

class welcomeActivity : AppCompatActivity() {
   private  val binding:ActivityWelcomeBinding by lazy {
       ActivityWelcomeBinding.inflate(layoutInflater)
   }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.loginButton.setOnClickListener {
            val intent = Intent(Intent(this,SignInAndRegistrationActivity::class.java))
            intent.putExtra("action","login")
            startActivity(intent)
        }

        binding.registerButton.setOnClickListener {
            val intent = Intent(Intent(this,SignInAndRegistrationActivity::class.java))
            intent.putExtra("action","register")
            startActivity(intent)
        }
    }
}