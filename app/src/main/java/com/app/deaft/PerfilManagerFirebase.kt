package com.app.deaft

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PerfilManagerFirebase {

    private val database: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun obterUsuarioAtual(): FirebaseUser? {
        return auth.currentUser
    }

    // Método para obter a referência do perfil do usuário atual
    fun obterPerfilReference(): DatabaseReference {
        val userId = auth.currentUser?.uid
        if (userId == null) {
            throw IllegalStateException("Usuário não autenticado")
        }

        val perfilReference = database.reference.child("perfis").child(userId)
        return perfilReference
    }

    // Método para salvar o perfil associado ao usuário atual
    fun salvarPerfil(perfil: Perfil, callback: () -> Unit) {
        Log.d("PerfilManagerFirebase", "Salvando perfil: $perfil")
        obterPerfilReference().setValue(perfil).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("PerfilManagerFirebase", "Perfil salvo com sucesso")
                callback()
            } else {
                // Trate o erro de salvamento aqui
                Log.e("PerfilManagerFirebase", "Erro ao salvar perfil", task.exception)
            }
        }
    }

    // Método para ler o perfil do usuário atual
    fun lerPerfil(callback: (Perfil) -> Unit) {
        obterPerfilReference().get().addOnSuccessListener { dataSnapshot ->
            val perfil = dataSnapshot.getValue(Perfil::class.java)
            callback(perfil ?: Perfil())
        }
    }

    // Método para preencher automaticamente o perfil associado ao usuário atual
    fun preencherAutomaticamentePerfil() {
        val user = obterUsuarioAtual()
        val perfilReference = obterPerfilReference()

        perfilReference.get().addOnSuccessListener { dataSnapshot ->
            if (!dataSnapshot.exists()) {
                // Preenche automaticamente o perfil com informações do Google
                val nome = user?.displayName ?: ""

                val perfil = Perfil(
                    nome = nome,
                    // Adicione outros campos conforme necessário
                )

                // Salva o perfil automaticamente
                salvarPerfil(perfil) {
                    // Opcional: Execute alguma ação após salvar o perfil
                }
            }
        }
    }
}
