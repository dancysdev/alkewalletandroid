package com.example.alkewallet.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.alkewallet.model.room.dao.ContactoDao
import com.example.alkewallet.model.room.dao.CuentaDao
import com.example.alkewallet.model.room.dao.MovimientoDao
import com.example.alkewallet.model.room.dao.TarjetaDao
import com.example.alkewallet.model.room.dao.UsuarioDao
import com.example.alkewallet.model.room.entity.ContactoEntity
import com.example.alkewallet.model.room.entity.CuentaEntity
import com.example.alkewallet.model.room.entity.MovimientoEntity
import com.example.alkewallet.model.room.entity.TarjetaEntity
import com.example.alkewallet.model.room.entity.UsuarioEntity

@Database(
    entities = [
        UsuarioEntity::class,
        CuentaEntity::class,
        TarjetaEntity::class,
        MovimientoEntity::class,
        ContactoEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    abstract fun cuentaDao(): CuentaDao

    abstract fun tarjetaDao(): TarjetaDao

    abstract fun movimientoDao(): MovimientoDao

    abstract fun contactoDao(): ContactoDao
}