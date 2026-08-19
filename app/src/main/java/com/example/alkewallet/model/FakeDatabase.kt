package com.example.alkewallet.model

object FakeDatabase {

    val usuarios = mutableListOf<Usuario>()

    fun inicializar() {
        if (usuarios.isEmpty()) {
            usuarios.addAll(MockUserDatabase.usuarios)
        }
    }
}