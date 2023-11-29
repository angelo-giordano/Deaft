package com.app.deaft

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.Calendar

class HomeFragment : Fragment() {

    private lateinit var btnEdit: Button
    private val perfilManager = PerfilManagerFirebase()
    private lateinit var textName: TextView
    private lateinit var age: TextView
    private lateinit var textDeafLevel: TextView
    private lateinit var textDescription: TextView
    private lateinit var btnLogout: Button
    private lateinit var auth: FirebaseAuth

    private val EDIT_PROFILE_REQUEST = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnEdit = view.findViewById(R.id.btnEditProfile)
        textName = view.findViewById(R.id.textName)
        age = view.findViewById(R.id.textAge)
        textDeafLevel = view.findViewById(R.id.textDeafLevel)
        textDescription = view.findViewById(R.id.textDescription)
        btnLogout = view.findViewById(R.id.btnLogout)
        auth = FirebaseAuth.getInstance()


        atualizarInformacoesPerfil()


        // Lógica específica do HomeFragment
        setupHomeFragment()
    }

    private fun setupHomeFragment() {
        btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(context, LoginActivity::class.java))
            activity?.finish()
        }

        btnEdit.setOnClickListener {
            startActivityForResult(Intent(context, EditPerfilActivity::class.java), EDIT_PROFILE_REQUEST)
        }
    }

    private fun atualizarInformacoesPerfil() {
        perfilManager.lerPerfil { perfil ->
            textName.text = perfil.nome
            age.text = perfil.idade.toString()
            textDeafLevel.text = perfil.nivelDeficiencia
            textDescription.text = perfil.sobreVoce
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("HomeFragment", "Editando")

        if (requestCode == EDIT_PROFILE_REQUEST && resultCode == Activity.RESULT_OK) {
            Log.d("HomeFragment", "Editou com sucesso")
            // Atualiza as informações do perfil após a edição
            atualizarInformacoesPerfil()
        }
    }
}

