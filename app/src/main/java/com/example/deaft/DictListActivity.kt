package com.example.deaft

import android.content.Intent
import android.os.Bundle
import android.os.RemoteCallbackList
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DictListActivity: AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var itemAdapter: DictAdapter
    private val itemList = mutableListOf<String>()
    private lateinit var btnSelectAdd: Button
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dict_list)

        recyclerView = findViewById(R.id.recyclerView)
        btnSelectAdd = findViewById(R.id.btn_select_add)

        databaseReference = FirebaseDatabase.getInstance().getReference("vibracoes-comunidade")

        itemAdapter = DictAdapter(itemList, this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = itemAdapter

        btnSelectAdd.setOnClickListener {
            val intent = Intent(this, DictAddActivity::class.java)
            startActivity(intent)
            finish()
        }

        listComunityWords()
    }

    private fun listComunityWords() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var words = mutableListOf<String>()

                // Iterar sobre os nós do Firebase e adicionar itens à lista
                for (dataSnapshot in snapshot.children) {
                    val key = dataSnapshot.key

                    if (key != null) {
                        words.add(key)
                    }
                }

                if (words.size == 1 && words[0] == "0") {
                    words[0] = "Não existem palavras cadastradas no momento."
                } else {
                    words = words.filter { it != "0" }.toMutableList()
                }


                itemList.clear()
                itemList.addAll(words)
                itemAdapter.notifyDataSetChanged()
            }


            override fun onCancelled(error: DatabaseError) {
                Log.e("MainActivity", "Erro ao consultar dados: ${error.message}")
            }
        })
    }
}