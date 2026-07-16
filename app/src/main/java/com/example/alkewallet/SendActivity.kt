package com.example.alkewallet

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SendActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_send)

        // Referencias
        val ivBackArrow = findViewById<ImageView>(R.id.ivBackArrow)
        val btnSend = findViewById<Button>(R.id.btnSend)

        // Regresar al home sin realizar ninguna transacción
        ivBackArrow.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        // Simular envío de dinero y volver al Home
        btnSend.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}