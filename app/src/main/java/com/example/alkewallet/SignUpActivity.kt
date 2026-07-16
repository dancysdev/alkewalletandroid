package com.example.alkewallet

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Referencias
        val btnMakeAccount = findViewById<Button>(R.id.btnMakeAccount)
        val tvYaTieneCuenta = findViewById<TextView>(R.id.tvYaTieneCuenta)

        // Crear cuenta → Home (decidirá aleatoriamente qué layout mostrar)
        btnMakeAccount.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        // Ya tiene cuenta → Login
        tvYaTieneCuenta.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}