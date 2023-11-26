package com.example.deaft

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DictAddActivity: AppCompatActivity() {

    private lateinit var spinner: Spinner
    private lateinit var btnAdd: Button
    private lateinit var databaseReference: DatabaseReference
    private lateinit var databaseReferenceUser: DatabaseReference




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_word)

        spinner = findViewById(R.id.spinner_words)
        btnAdd = findViewById(R.id.btn_add_word)
        databaseReference = FirebaseDatabase.getInstance().getReference("vibracoes-opcoes")
        databaseReferenceUser = FirebaseDatabase.getInstance().getReference("vibracoes-comunidade")

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        loadOptions()

        btnAdd.setOnClickListener {
            val newWord = spinner.selectedItem.toString()

            addNewWord(newWord)

            val intent = Intent(this, DictListActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun loadOptions() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val wordsList = mutableListOf<String>()

                // Iterar sobre os nós do Firebase e adicionar IDs à lista
                for (dataSnapshot in snapshot.children) {
                    val word = dataSnapshot.key
                    word?.let { wordsList.add(it) }
                }

                // Criar ArrayAdapter e associá-lo ao Spinner
                val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
                    this@DictAddActivity,
                    android.R.layout.simple_spinner_item,
                    wordsList
                )

                // Correção: Usar o layout correto para o menu suspenso
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                spinner.adapter = arrayAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Tratar erro, se necessário
            }
        })
    }

    private fun addNewWord(word: String) {
        // Gere uma chave única para o novo item
        databaseReference.child(word).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val vibration = snapshot.getValue(String::class.java)

                if (vibration != null) {
                    val newKeyUser = databaseReferenceUser.child(word)
                    newKeyUser.setValue(vibration)
                        .addOnSuccessListener {
                            // Sucesso ao adicionar o item à tabela de destino
                            // Adicione qualquer lógica adicional aqui
                        }
                        .addOnFailureListener {
                            // Falha ao adicionar o item à tabela de destino
                            // Trate a falha conforme necessário
                    }
            } else {

            }

        }
            override fun onCancelled(error: DatabaseError) {
                // Tratar erro na leitura da outra tabela, se necessário
            }
        })
    }
}
