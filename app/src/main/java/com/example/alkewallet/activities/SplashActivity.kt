package com.example.alkewallet.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.alkewallet.R

import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.alkewallet.controller.ApiTestController
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({

            val intent = Intent(this, LoginSignupActivity::class.java)
            startActivity(intent)
            finish()

        }, 2000)

    }
}