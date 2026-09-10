package com.example.applista.dao

import com.example.applista.model.Usuario

class UsuarioDao {
    companion object{
        private val usuarios = mutableListOf<Usuario>()

        fun salvar(nome:String):String {
            usuarios.add(Usuario(nome))
            return  "Usuario Salva"
        }

        fun buscar():List<Usuario> {
            return usuarios
        }
    }
}