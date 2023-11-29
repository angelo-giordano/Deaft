package com.app.deaft

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get


class EditPerfilActivity : AppCompatActivity() {
    private lateinit var textName: EditText
    private lateinit var textAge: EditText
    private lateinit var textDescription: EditText
    private lateinit var textDeafLevel: Spinner
    private lateinit var btnSaveProfile: Button
    private lateinit var perfilManagerFirebase: PerfilManagerFirebase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        textName = findViewById(R.id.textName)
        textAge = findViewById(R.id.textAge)
        textDescription = findViewById(R.id.textDescription)
        btnSaveProfile = findViewById(R.id.btnSaveProfile)

        perfilManagerFirebase = PerfilManagerFirebase()

        val deafLvl = arrayOf("Audição Normal", "Perda Auditiva Leve", "Perda Auditiva Moderada",
            "Perda Auditiva Severa", "Perda Auditiva Profunda", "Perda Auditiva Total")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, deafLvl)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        textDeafLevel = findViewById(R.id.textDeafLevel)
        textDeafLevel.adapter = adapter

        perfilManagerFirebase.lerPerfil { perfil: Perfil ->
            textName.setText(perfil.nome)
            textAge.setText(perfil.idade.toString())
            textDescription.setText(perfil.sobreVoce)
            val pos = deafLvl.indexOf(perfil.nivelDeficiencia)
            textDeafLevel.setSelection(pos)
        }


        btnSaveProfile.setOnClickListener {
            val newName = textName.text.toString()
            val newAge = textAge.text.toString()
            val newDescription = textDescription.text.toString()
            val newDeafLevel = textDeafLevel.selectedItem.toString()

            val editedPerfil = Perfil(
                nome = newName,
                idade = newAge.toIntOrNull() ?: 0,
                sobreVoce = newDescription,
                nivelDeficiencia = newDeafLevel
            )
            perfilManagerFirebase.salvarPerfil(editedPerfil){
                setResult(RESULT_OK)
                finish()
            }
        }

    }
}
