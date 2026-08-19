package com.example.alkewallet.activities


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alkewallet.R
import com.example.alkewallet.adapter.ContactAdapter
import com.example.alkewallet.model.FakeContactDatabase
import com.example.alkewallet.model.FakeDatabase
import com.example.alkewallet.model.Usuario
import com.example.alkewallet.utils.SessionManager

class ContactsActivity : AppCompatActivity() {

    private lateinit var adapter: ContactAdapter

    // Lista completa de contactos del usuario actual
    private var contactosActuales = mutableListOf<Usuario>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)


        // Referencias
        val ivBackArrow = findViewById<ImageView>(R.id.ivBackArrow)
        val etBuscarContacto =
            findViewById<EditText>(R.id.etBuscarContacto)
        val ivSearch =
            findViewById<ImageView>(R.id.ivSearch)

        val recyclerContactos =
            findViewById<RecyclerView>(R.id.recyclerContactos)

        val etContactNumber =
            findViewById<EditText>(R.id.etContactNumber)

        val btnAddContact =
            findViewById<Button>(R.id.btnAddContact)

        val btnLessContact =
            findViewById<Button>(R.id.btnLessContact)


        // Usuario actualmente logueado
        val usuarioActual = SessionManager.usuarioActual

        if (usuarioActual == null) {
            finish()
            return
        }


        // RecyclerView
        recyclerContactos.layoutManager =
            LinearLayoutManager(this)

        adapter = ContactAdapter(
            contactosActuales
        ) { contactoSeleccionado ->

            // Contacto seleccionado → SendActivity
            val intent = Intent(
                this,
                SendActivity::class.java
            )

            intent.putExtra(
                "alkeNumeroContacto",
                contactoSeleccionado.alkeNumero
            )

            startActivity(intent)
        }

        recyclerContactos.adapter = adapter


        // Cargar contactos iniciales
        cargarContactos(usuarioActual.alkeNumero)


        // =========================
        // VOLVER
        // =========================

        ivBackArrow.setOnClickListener {
            finish()
        }


        // =========================
        // BUSCAR
        // =========================

        ivSearch.setOnClickListener {

            val textoBusqueda =
                etBuscarContacto.text.toString().trim()

            filtrarContactos(textoBusqueda)
        }


        // =========================
        // AÑADIR CONTACTO
        // =========================

        btnAddContact.setOnClickListener {

            val alkeNumero =
                etContactNumber.text.toString().trim().uppercase()

            if (alkeNumero.isEmpty()) {

                Toast.makeText(
                    this,
                    "Introduce un AlkeNúmero",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }


            // No puede añadirse a sí mismo
            if (alkeNumero == usuarioActual.alkeNumero) {

                Toast.makeText(
                    this,
                    "No puedes añadirte a ti mismo",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }


            // Buscar usuario real en FakeDatabase
            val usuarioEncontrado =
                FakeDatabase.usuarios.find {
                    it.alkeNumero == alkeNumero
                }


            if (usuarioEncontrado == null) {

                Toast.makeText(
                    this,
                    "No existe un usuario con ese AlkeNúmero",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }


            // Obtener o crear lista de contactos
            val listaContactos =
                FakeContactDatabase.contactosPorUsuario
                    .getOrPut(usuarioActual.alkeNumero) {
                        mutableListOf()
                    }


            // Verificar si ya existe
            if (listaContactos.contains(alkeNumero)) {

                Toast.makeText(
                    this,
                    "Este contacto ya está añadido",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }


            // Añadir AlkeNúmero
            listaContactos.add(alkeNumero)


            Toast.makeText(
                this,
                "Contacto añadido",
                Toast.LENGTH_SHORT
            ).show()


            // Actualizar lista
            cargarContactos(usuarioActual.alkeNumero)

            etContactNumber.text.clear()
        }


        // =========================
        // ELIMINAR CONTACTO
        // =========================

        btnLessContact.setOnClickListener {

            val alkeNumero =
                etContactNumber.text.toString().trim().uppercase()

            if (alkeNumero.isEmpty()) {

                Toast.makeText(
                    this,
                    "Introduce un AlkeNúmero",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }


            val listaContactos =
                FakeContactDatabase.contactosPorUsuario[
                    usuarioActual.alkeNumero
                ]


            if (listaContactos == null ||
                !listaContactos.contains(alkeNumero)
            ) {

                Toast.makeText(
                    this,
                    "El contacto no existe en tu lista",
                    Toast.LENGTH_SHORT
                ).show()

                return@setOnClickListener
            }


            listaContactos.remove(alkeNumero)


            Toast.makeText(
                this,
                "Contacto eliminado",
                Toast.LENGTH_SHORT
            ).show()


            // Actualizar lista
            cargarContactos(usuarioActual.alkeNumero)

            etContactNumber.text.clear()
        }
    }


    // =====================================================
    // CARGAR CONTACTOS
    // =====================================================

    private fun cargarContactos(
        alkeNumeroUsuario: String
    ) {

        contactosActuales.clear()


        val numerosContactos =
            FakeContactDatabase.contactosPorUsuario[
                alkeNumeroUsuario
            ]


        if (numerosContactos != null) {

            for (numero in numerosContactos) {

                val usuario =
                    FakeDatabase.usuarios.find {
                        it.alkeNumero == numero
                    }

                if (usuario != null) {
                    contactosActuales.add(usuario)
                }
            }
        }


        adapter.notifyDataSetChanged()
    }


    // =====================================================
    // FILTRAR CONTACTOS
    // =====================================================

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
}

