package com.example.myapplication.dao
import com.example.myapplication.model.Usuario

class LoginDao {

    companion object {
        private val usuarioValido = Usuario.Usuario("admin", "1234")

        fun validar(login: String, senha: String) : Boolean {
            return login == usuarioValido.login && senha == usuarioValido.senha
        }
    }
}