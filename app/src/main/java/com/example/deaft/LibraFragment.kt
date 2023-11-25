package com.example.deaft

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.google.common.util.concurrent.SettableFuture
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class LibraFragment : Fragment() {

    private lateinit var inputText: AutoCompleteTextView
    private lateinit var searchBtn: Button
    private lateinit var librasView: ImageView
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        databaseReference = FirebaseDatabase.getInstance().getReference("gifs_libra")
        return inflater.inflate(R.layout.fragment_libra, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchBtn = view.findViewById(R.id.searchBtn)
        inputText = view.findViewById(R.id.inputText)
        librasView = view.findViewById(R.id.gifLibras)
        configureSuggestions()
        setupLibraFragment()
    }

    private fun setupLibraFragment() {
        // Implemente a lógica específica do HomeFragment aqui
        searchBtn.setOnClickListener {
            val gifId = inputText.text.toString()
            val gifUrl = getGifUrlById(gifId)

            Futures.addCallback(gifUrl, object : FutureCallback<String?> {
                override fun onSuccess(result: String?) {
                    // Faça algo com o URL do GIF aqui
                    // Carregue o GIF no ImageView usando Glide
                    if (!result.isNullOrEmpty()) {
                        Glide.with(requireContext())
                            .load(result)
                            .into(librasView)
                    }
                }

                override fun onFailure(t: Throwable) {
                    // Lidar com falhas, se necessário
                }
            }, MoreExecutors.directExecutor())
        }
    }

    private fun configureSuggestions() {
        val gifIdList = ArrayList<String>()

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (gifSnapshot in dataSnapshot.children) {
                    val gifId = gifSnapshot.key
                    gifId?.let { gifIdList.add(it) }
                }

                // Configurar o adaptador para sugestões
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, gifIdList)
                inputText.setAdapter(adapter)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Lidar com erros de leitura do banco de dados, se necessário
            }
        })
    }

    private fun getGifUrlById(gifId: String): ListenableFuture<String?> {
        // Apontar para o nó específico com base no ID
        val gifNodeReference = databaseReference.child(gifId)
        val future = SettableFuture.create<String?>()

        // Adicionar um listener para obter o URL do GIF
        gifNodeReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Verificar se o nó existe
                if (dataSnapshot.exists()) {
                    // Obter o URL do GIF
                    val gifUrl = dataSnapshot.getValue(String::class.java)
                    future.set(gifUrl)
                } else {
                    // O nó com o ID fornecido não existe
                    future.set(null)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                future.setException(databaseError.toException())            }
        })

        return future
    }

}