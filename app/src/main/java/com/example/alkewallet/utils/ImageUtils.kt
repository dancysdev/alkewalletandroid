package com.example.alkewallet.utils

import android.net.Uri
import android.widget.ImageView
import com.example.alkewallet.R

object ImageUtils {

    fun cargarImagenPerfil(
        imageView: ImageView,
        imagenPerfil: String?
    ) {

        if (imagenPerfil == "default_profile") {

            imageView.setImageResource(
                R.drawable.user_default
            )

            return
        }

        if (imagenPerfil.isNullOrEmpty()) {

            imageView.setImageResource(
                R.drawable.user_default
            )

            return
        }

        try {

            val resourceId = imageView.resources.getIdentifier(
                imagenPerfil,
                "drawable",
                imageView.context.packageName
            )

            if (resourceId != 0) {

                imageView.setImageResource(resourceId)

            } else {

                imageView.setImageURI(
                    Uri.parse(imagenPerfil)
                )
            }

        } catch (e: Exception) {

            imageView.setImageResource(
                R.drawable.user_default
            )
        }
    }
}