package com.example.alkewallet

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class RequestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request)

        // Referencias
        val ivBackArrow = findViewById<ImageView>(R.id.ivBackArrow)
        val btnRequest = findViewById<Button>(R.id.btnRequest)

        // Regresar al Home sin solicitar dinero
        ivBackArrow.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        // Simular solicitud de dinero y volver al Home
        btnRequest.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}