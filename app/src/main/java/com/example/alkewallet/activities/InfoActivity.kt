package com.example.alkewallet.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.alkewallet.R
import com.example.alkewallet.controller.UserController
import com.example.alkewallet.utils.ImageUtils
import com.example.alkewallet.utils.SessionManager
import kotlinx.coroutines.launch

class InfoActivity : AppCompatActivity() {

    // Estado de la pantalla
    private var modoEdicion = false

    // Imagen seleccionada durante la edición.
    // Solo se guarda definitivamente al pulsar guardar.
    private var imagenSeleccionadaUri: Uri? = null

    // =====================================================
    // SELECTOR DE IMÁGENES
    // =====================================================

    private val selectorImagen =
        registerForActivityResult(
            ActivityResultContracts.OpenDocument()
        ) { uri ->

            if (uri != null) {

                // Conservar permiso de lectura de la imagen
                try {
                    contentResolver.takePersistableUriPermission(
                        uri,
                        Intent.FLAG_GRANT_READ_URI_PERMISSION
                    )
                } catch (e: SecurityException) {
                    // Algunos proveedores no permiten permisos persistentes.
                    // En ese caso simplemente continuamos.
                }

                // Guardar temporalmente la imagen seleccionada
                imagenSeleccionadaUri = uri

                // Mostrar inmediatamente la imagen
                findViewById<ImageView>(
                    R.id.ivAvatar
                ).setImageURI(uri)
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        // =================================================
        // REFERENCIAS
        // =================================================

        val ivBackArrow =
            findViewById<ImageView>(R.id.ivBackArrow)

        val ivEditar =
            findViewById<ImageView>(R.id.ivEditar)

        val ivAvatar =
            findViewById<ImageView>(R.id.ivAvatar)

        val tvAvatar =
            findViewById<TextView>(R.id.tvAvatar)

        val etNombre =
            findViewById<EditText>(R.id.etNombre)

        val etApellido =
            findViewById<EditText>(R.id.etApellido)

        val etCorreo =
            findViewById<EditText>(R.id.etCorreo)

        val tvNumeroAlke =
            findViewById<TextView>(R.id.tvNumeroAlke)

        val tvPuntosAlke =
            findViewById<TextView>(R.id.tvPuntosAlke)


        // =================================================
        // USUARIO DE LA SESIÓN
        // =================================================

        val usuario = SessionManager.usuarioActual

        if (usuario == null) {
            finish()
            return
        }


        // =================================================
        // CARGAR DATOS DEL USUARIO
        // =================================================

        etNombre.setText(usuario.nombre)

        etApellido.setText(usuario.apellido)

        etCorreo.setText(usuario.correo)

        tvAvatar.text =
            "${usuario.nombre} ${usuario.apellido}"

        tvNumeroAlke.text =
            usuario.alkeNumero

        // Puntos de maqueta
        tvPuntosAlke.text = "1.250"


        // =================================================
        // CARGAR IMAGEN DE PERFIL
        // =================================================

        ImageUtils.cargarImagenPerfil(
            ivAvatar,
            usuario.imagenPerfil
        )

        // =================================================
        // ESTADO INICIAL
        // =================================================

        bloquearCampos(
            etNombre,
            etApellido,
            etCorreo
        )


        // =================================================
        // VOLVER A PROFILE
        // =================================================

        ivBackArrow.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    ProfileActivity::class.java
                )
            )

            finish()
        }


        // =================================================
        // EDITAR / GUARDAR
        // =================================================

        ivEditar.setOnClickListener {

            if (!modoEdicion) {

                // =========================================
                // MODO EDICIÓN
                // =========================================

                modoEdicion = true

                habilitarCampos(
                    etNombre,
                    etApellido,
                    etCorreo
                )

                // Permitir seleccionar imagen
                ivAvatar.isClickable = true

                etNombre.requestFocus()

            } else {

                // =========================================
                // GUARDAR
                // =========================================

                // Guardar datos personales

                usuario.nombre =
                    etNombre.text.toString()

                usuario.apellido =
                    etApellido.text.toString()

                usuario.correo =
                    etCorreo.text.toString()


                // -----------------------------------------
                // Guardar imagen
                // -----------------------------------------

                if (imagenSeleccionadaUri != null) {

                    usuario.imagenPerfil =
                        imagenSeleccionadaUri.toString()
                }


                // -----------------------------------------
                // Actualizar Room
                // -----------------------------------------

                lifecycleScope.launch {

                    val userController =
                        UserController(this@InfoActivity)

                    val actualizado =
                        userController.editarPerfil(usuario)

                    if (!actualizado) {
                        return@launch
                    }

                    // -------------------------------------
                    // Cerrar teclado
                    // -------------------------------------

                    quitarFocoYTeclado()


                    // -------------------------------------
                    // Volver a ProfileActivity
                    // -------------------------------------

                    startActivity(
                        Intent(
                            this@InfoActivity,
                            ProfileActivity::class.java
                        )
                    )

                    // Destruir InfoActivity
                    finish()
                }
            }
        }


        // =================================================
        // SELECCIONAR IMAGEN
        // =================================================

        ivAvatar.setOnClickListener {

            if (modoEdicion) {

                selectorImagen.launch(
                    arrayOf("image/*")
                )
            }
        }
    }


    // =====================================================
    // HABILITAR CAMPOS
    // =====================================================

    private fun habilitarCampos(
        etNombre: EditText,
        etApellido: EditText,
        etCorreo: EditText
    ) {

        etNombre.isFocusable = true
        etNombre.isFocusableInTouchMode = true
        etNombre.isCursorVisible = true

        etApellido.isFocusable = true
        etApellido.isFocusableInTouchMode = true
        etApellido.isCursorVisible = true

        etCorreo.isFocusable = true
        etCorreo.isFocusableInTouchMode = true
        etCorreo.isCursorVisible = true
    }


    // =====================================================
    // BLOQUEAR CAMPOS
    // =====================================================

    private fun bloquearCampos(
        etNombre: EditText,
        etApellido: EditText,
        etCorreo: EditText
    ) {

        etNombre.isFocusable = false
        etNombre.isFocusableInTouchMode = false
        etNombre.isCursorVisible = false

        etApellido.isFocusable = false
        etApellido.isFocusableInTouchMode = false
        etApellido.isCursorVisible = false

        etCorreo.isFocusable = false
        etCorreo.isFocusableInTouchMode = false
        etCorreo.isCursorVisible = false
    }


    // =====================================================
    // QUITAR FOCO Y TECLADO
    // =====================================================

    private fun quitarFocoYTeclado() {

        // Quitar foco del campo actual
        currentFocus?.clearFocus()

        // Ocultar teclado
        val imm = getSystemService(
            INPUT_METHOD_SERVICE
        ) as InputMethodManager

        imm.hideSoftInputFromWindow(
            window.decorView.windowToken,
            0
        )
    }
}

