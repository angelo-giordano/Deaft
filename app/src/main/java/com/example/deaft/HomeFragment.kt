package com.example.deaft

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.Calendar

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            // Obtém o nome do usuário

            // Atualiza uma TextView (substitua com o ID correto do seu TextView)
            val textViewNameUser: TextView = view.findViewById(R.id.textName)
            textViewNameUser.setText(user.displayName)
        }

        // Lógica específica do HomeFragment
        setupHomeFragment()
    }

    private fun setupHomeFragment() {
        // Implemente a lógica específica do HomeFragment aqui
        //textViewHome.text = "Bem-vindo à tela Home!"
        // ...
    }

    fun calcularIdade(dataNascimentoMillis: Long): Int {
        val hoje = Calendar.getInstance().time
        val diferenca = hoje.time - dataNascimentoMillis

        val idade = Calendar.getInstance().apply {
            timeInMillis = diferenca
        }.get(Calendar.YEAR) - 1970

        return idade
    }
}

