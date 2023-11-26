package com.example.deaft

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

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
            val nomeUsuario = user.displayName

            // Atualiza uma TextView (substitua com o ID correto do seu TextView)
            val textViewNameUser: TextView = view.findViewById(R.id.textName)
            val textViewEmailUser: TextView = view.findViewById(R.id.textUsername)
            textViewNameUser.setText(user.displayName)
            textViewEmailUser.setText(user.email)
        }

        // Lógica específica do HomeFragment
        setupHomeFragment()
    }

    private fun setupHomeFragment() {
        // Implemente a lógica específica do HomeFragment aqui
        //textViewHome.text = "Bem-vindo à tela Home!"
        // ...
    }
}

