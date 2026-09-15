package com.example.alkewallet.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alkewallet.R
import com.example.alkewallet.adapter.ContactAdapter
import com.example.alkewallet.controller.UserController
import com.example.alkewallet.model.Usuario
import com.example.alkewallet.utils.SessionManager
import kotlinx.coroutines.launch

class ContactsActivity : AppCompatActivity() {

    private lateinit var adapter: ContactAdapter

    private val contactosActuales =
        mutableListOf<Usuario>()

    private val userController by lazy {
        UserController(this)
    }


    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_contact
        )


        // =====================================================
        // REFERENCIAS VISTA
        // =====================================================

        val ivBackArrow =
            findViewById<ImageView>(
                R.id.ivBackArrow
            )

        val etBuscarContacto =
            findViewById<EditText>(
                R.id.etBuscarContacto
            )

        val ivSearch =
            findViewById<ImageView>(
                R.id.ivSearch
            )

        val recyclerContactos =
            findViewById<RecyclerView>(
                R.id.recyclerContactos
            )

        val etContactNumber =
            findViewById<EditText>(
                R.id.etContactNumber
            )

        val btnAddContact =
            findViewById<Button>(
                R.id.btnAddContact
            )

        val btnLessContact =
            findViewById<Button>(
                R.id.btnLessContact
            )


        // =====================================================
        // USUARIO ACTUAL
        // =====================================================

        val usuarioActual =
            SessionManager.usuarioActual

        if (usuarioActual == null) {
            finish()
            return
        }


        // =====================================================
        // RECYCLERVIEW
        // =====================================================

        recyclerContactos.layoutManager =
            LinearLayoutManager(this)

        adapter =
            ContactAdapter(
                contactosActuales
            ) { contactoSeleccionado ->

                val intent =
                    Intent(
                        this,
                        SendActivity::class.java
                    )

                intent.putExtra(
                    "alkeNumeroContacto",
                    contactoSeleccionado.alkeNumero
                )

                startActivity(intent)
            }

        recyclerContactos.adapter =
            adapter


        // =====================================================
        // CARGAR CONTACTOS
        // =====================================================

        lifecycleScope.launch {

            cargarContactos(
                usuarioActual.id
            )
        }


        // =====================================================
        // VOLVER
        // =====================================================

        ivBackArrow.setOnClickListener {

            finish()
        }


        // =====================================================
        // BUSCAR CONTACTO
        // =====================================================

        ivSearch.setOnClickListener {

            val textoBusqueda =
                etBuscarContacto.text
                    .toString()
                    .trim()

            filtrarContactos(
                textoBusqueda
            )
        }


        // =====================================================
        // AGREGAR CONTACTO
        // =====================================================

        btnAddContact.setOnClickListener {

            val texto =
                etContactNumber.text
                    .toString()
                    .trim()

            if (texto.isEmpty()) {

                Toast.makeText(
                    this,
                    "Introduce un número ALKE",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }


            val alkeNumero =
                normalizarAlkeNumero(
                    texto
                )


            if (
                alkeNumero ==
                usuarioActual.alkeNumero
            ) {

                Toast.makeText(
                    this,
                    "No puedes agregarte a ti mismo",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }


            lifecycleScope.launch {

                // ---------------------------------------------
                // Buscar usuario en backend
                // ---------------------------------------------

                val usuarioRemoto =
                    userController.buscarUsuarioRemotoPorAlke(
                        alkeNumero
                    )


                if (usuarioRemoto == null) {

                    Toast.makeText(
                        this@ContactsActivity,
                        "No existe un usuario con ese número ALKE",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@launch
                }


                // ---------------------------------------------
                // Guardar contacto en Room
                // ---------------------------------------------

                val agregado =
                    userController.agregarContacto(
                        usuarioId = usuarioActual.id,
                        alkeNumero = alkeNumero
                    )


                if (!agregado) {

                    Toast.makeText(
                        this@ContactsActivity,
                        "El contacto ya está agregado",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@launch
                }


                Toast.makeText(
                    this@ContactsActivity,
                    "Contacto añadido",
                    Toast.LENGTH_SHORT
                ).show()


                // ---------------------------------------------
                // Recargar lista
                // ---------------------------------------------

                cargarContactos(
                    usuarioActual.id
                )

                etContactNumber.text.clear()
            }
        }


        // =====================================================
        // ELIMINAR CONTACTO
        // =====================================================

        btnLessContact.setOnClickListener {

            val texto =
                etContactNumber.text
                    .toString()
                    .trim()

            if (texto.isEmpty()) {

                Toast.makeText(
                    this,
                    "Introduce un número ALKE",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }


            val alkeNumero =
                normalizarAlkeNumero(
                    texto
                )


            lifecycleScope.launch {

                val eliminado =
                    userController.eliminarContacto(
                        usuarioId = usuarioActual.id,
                        alkeNumero = alkeNumero
                    )


                if (!eliminado) {

                    Toast.makeText(
                        this@ContactsActivity,
                        "Ese contacto no está en tu lista",
                        Toast.LENGTH_SHORT
                    ).show()

                    return@launch
                }


                Toast.makeText(
                    this@ContactsActivity,
                    "Contacto eliminado",
                    Toast.LENGTH_SHORT
                ).show()


                cargarContactos(
                    usuarioActual.id
                )

                etContactNumber.text.clear()
            }
        }
    }


    // =========================================================
    // CARGAR CONTACTOS DESDE ROOM
    // =========================================================

    private suspend fun cargarContactos(
        usuarioId: Int
    ) {

        contactosActuales.clear()


        val contactos =
            userController.listarContactos(
                usuarioId
            )


        for (contacto in contactos) {

            val usuario =
                userController.buscarUsuarioPorAlke(
                    contacto.alkeNumero
                )

            if (usuario != null) {

                contactosActuales.add(
                    usuario
                )
            }
        }


        adapter.notifyDataSetChanged()
    }


    // =========================================================
    // FILTRAR CONTACTOS
    // =========================================================

    private fun filtrarContactos(
        textoBusqueda: String
    ) {

        val contactosFiltrados =

            if (textoBusqueda.isEmpty()) {

                contactosActuales.toList()

            } else {

                contactosActuales.filter {

                    val nombreCompleto =
                        "${it.nombre} ${it.apellido}"

                    nombreCompleto.contains(
                        textoBusqueda,
                        ignoreCase = true
                    ) ||

                            it.correo.contains(
                                textoBusqueda,
                                ignoreCase = true
                            ) ||

                            it.alkeNumero.contains(
                                textoBusqueda,
                                ignoreCase = true
                            )
                }
            }


        adapter.actualizarContactos(
            contactosFiltrados
        )
    }


    // =========================================================
    // NORMALIZAR NÚMERO ALKE
    // =========================================================

    private fun normalizarAlkeNumero(
        texto: String
    ): String {

        val valor =
            texto
                .trim()
                .uppercase()


        return if (
            valor.startsWith("ALKE")
        ) {

            valor

        } else {

            "ALKE$valor"
        }
    }
}

