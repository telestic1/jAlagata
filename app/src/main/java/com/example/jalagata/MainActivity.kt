package com.example.jalagata

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.jalagata.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private  val binding : ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

binding.floatingActionButton.setOnClickListener {
    startActivity(Intent(this,AddArticleActivity::class.java))

}


    }
}