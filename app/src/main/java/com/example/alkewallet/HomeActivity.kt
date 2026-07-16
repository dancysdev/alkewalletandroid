package com.example.alkewallet
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val hayTransacciones = Random.nextBoolean()

        if (hayTransacciones) {
            setContentView(R.layout.activity_home)
        } else {
            setContentView(R.layout.activity_empty_case)
        }

        // Referencias comunes a ambos layouts
        val btnEnviarDinero = findViewById<Button>(R.id.btnEnviarDinero)
        val btnIngresarDinero = findViewById<Button>(R.id.btnIngresarDinero)
        val imgPerfil = findViewById<ImageView>(R.id.imgPerfil)

        // Enviar dinero
        btnEnviarDinero.setOnClickListener {
            startActivity(Intent(this, SendActivity::class.java))
        }

        // Ingresar dinero
        btnIngresarDinero.setOnClickListener {
            startActivity(Intent(this, RequestActivity::class.java))
        }

        // Perfil
        imgPerfil.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
}