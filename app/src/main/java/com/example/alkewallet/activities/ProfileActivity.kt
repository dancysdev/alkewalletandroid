package com.example.alkewallet.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.alkewallet.R
import com.example.alkewallet.utils.SessionManager

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // =================================================
        // REFERENCIAS
        // =================================================

        val ivBackArrow =
            findViewById<ImageView>(R.id.ivBackArrow)

        val ivAvatar =
            findViewById<ImageView>(R.id.ivAvatar)

        val tvNameProfile =
            findViewById<TextView>(R.id.tvNameProfile)

        val btnMiInformacion =
            findViewById<Button>(R.id.btnInfo)

        val btnCloseSession =
            findViewById<Button>(R.id.btnCloseSession)

        val btnCards =
            findViewById<Button>(R.id.btnCards)


        // =================================================
        // USUARIO ACTUAL
        // =================================================

        val usuario =
            SessionManager.usuarioActual

        if (usuario == null) {
            finish()
            return
        }


        // =================================================
        // NOMBRE DEL USUARIO
        // =================================================

        tvNameProfile.text =
            "${usuario.nombre} ${usuario.apellido}"


        // =================================================
        // IMAGEN DE PERFIL
        // =================================================

        if (usuario.imagenPerfil == "default_profile") {

            ivAvatar.setImageResource(
                R.drawable.user_default
            )

        } else if (!usuario.imagenPerfil.isNullOrEmpty()) {

            try {

                val resourceId = resources.getIdentifier(
                    usuario.imagenPerfil,
                    "drawable",
                    packageName
                )

                if (resourceId != 0) {

                    // Imagen perteneciente a drawable
                    ivAvatar.setImageResource(resourceId)

                } else {

                    // Imagen seleccionada desde el dispositivo
                    ivAvatar.setImageURI(
                        Uri.parse(usuario.imagenPerfil)
                    )
                }

            } catch (e: Exception) {

                ivAvatar.setImageResource(
                    R.drawable.user_default
                )
            }

        } else {

            ivAvatar.setImageResource(
                R.drawable.user_default
            )
        }


        // =================================================
        // REGRESAR AL HOME
        // =================================================

        ivBackArrow.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    HomeActivity::class.java
                )
            )

            finish()
        }


        // =================================================
        // IR A INFORMACIÓN PERSONAL
        // =================================================

        btnMiInformacion.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    InfoActivity::class.java
                )
            )
        }


        // =================================================
        // IR A MIS TARJETAS
        // =================================================

        btnCards.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    CardsActivity::class.java
                )
            )
        }


        // =================================================
        // CERRAR SESIÓN
        // =================================================

        btnCloseSession.setOnClickListener {

            SessionManager.cerrarSesion()

            startActivity(
                Intent(
                    this,
                    LoginActivity::class.java
                )
            )

            finish()
        }
    }
}