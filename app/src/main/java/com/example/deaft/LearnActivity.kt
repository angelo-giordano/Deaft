package com.example.deaft


import android.os.Bundle
import com.chaquo.python.Python
import java.util.*
import android.widget.Button
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.chaquo.python.android.AndroidPlatform
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class LearnActivity : AppCompatActivity() {

    private lateinit var inputText: AutoCompleteTextView
    private lateinit var searchBtn: Button
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)

        searchBtn = findViewById(R.id.searchBtn)
        inputText = findViewById(R.id.inputTextLearn)
        databaseReference = FirebaseDatabase.getInstance().getReference("vibracoes")

        configureSuggestions()

        searchBtn.setOnClickListener {
            val text = inputText.text.toString().trim().lowercase()
            performAction(text)
        }

    }

    private fun performAction(text: String) {
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }

        GlobalScope.launch {
            val processedText = VibrationUtil.translateToVib(text)
            processedText.forEach { element ->
                VibrationUtil.vibrate(this@LearnActivity, element)
                delay(500)
            }
        }
    }

    private fun configureSuggestions() {
        val vibrationIdList = ArrayList<String>()

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (vibrationSnapshot in dataSnapshot.children) {
                    val vibrationId = vibrationSnapshot.key
                    vibrationId?.let { vibrationIdList.add(it) }
                }

                // Configurar o adaptador para sugestões
                val adapter = ArrayAdapter(this@LearnActivity, android.R.layout.simple_dropdown_item_1line, vibrationIdList)
                inputText.setAdapter(adapter)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Lidar com erros de leitura do banco de dados, se necessário
            }
        })
    }
}

