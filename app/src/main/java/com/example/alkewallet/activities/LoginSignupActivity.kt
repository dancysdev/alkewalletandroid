package com.example.alkewallet.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.alkewallet.R

class LoginSignupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_signup)

        val btnCrearCuenta = findViewById<Button>(R.id.btnCrearCuenta)
        val tvYaTieneCuenta = findViewById<TextView>(R.id.tvYaTieneCuenta)

        btnCrearCuenta.setOnClickListener {

            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)

        }

        tvYaTieneCuenta.setOnClickListener {

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }
    }
}