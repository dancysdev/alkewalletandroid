package com.example.alkewallet.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.alkewallet.R
import com.example.alkewallet.model.Usuario
import com.example.alkewallet.utils.ImageUtils

class ContactAdapter(
    private val contactos: MutableList<Usuario>,
    private val onContactoClick: (Usuario) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    class ContactViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val imgUsuario = itemView.findViewById<ImageView>(
            R.id.imgUsuarioMovimiento
        )

        val tvNombre = itemView.findViewById<TextView>(
            R.id.tvNombreContacto
        )

        val tvEmail = itemView.findViewById<TextView>(
            R.id.tvEmail
        )
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.item_contact,
                parent,
                false
            )

        return ContactViewHolder(view)
    }


    override fun onBindViewHolder(
        holder: ContactViewHolder,
        position: Int
    ) {

        val contacto = contactos[position]

        holder.tvNombre.text =
            "${contacto.nombre} ${contacto.apellido}"

        holder.tvEmail.text =
            contacto.correo


        // =================================================
        // IMAGEN DEL CONTACTO
        // =================================================

        ImageUtils.cargarImagenPerfil(
            holder.imgUsuario,
            contacto.imagenPerfil
        )


        // =================================================
        // SELECCIONAR CONTACTO
        // =================================================

        holder.itemView.setOnClickListener {

            onContactoClick(contacto)
        }
    }


    override fun getItemCount(): Int {
        return contactos.size
    }


    // =================================================
    // ACTUALIZAR CONTACTOS MOSTRADOS
    // =================================================

    fun actualizarContactos(
        nuevosContactos: List<Usuario>
    ) {

        contactos.clear()
        contactos.addAll(nuevosContactos)

        notifyDataSetChanged()
    }
}




