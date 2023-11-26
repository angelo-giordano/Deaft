package com.example.deaft


import android.os.Bundle
import com.chaquo.python.Python
import java.util.*
import android.widget.Button
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.chaquo.python.android.AndroidPlatform
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class LearnActivity : AppCompatActivity() {

    private lateinit var inputText: AutoCompleteTextView
    private lateinit var searchBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)

        searchBtn = findViewById(R.id.searchBtn)
        inputText = findViewById(R.id.inputTextLearn)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

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
        val databaseReferenceGeneral = FirebaseDatabase.getInstance().getReference("vibracoes")
        val databaseReferenceComunity = FirebaseDatabase.getInstance().getReference("vibracoes-comunidade")

        databaseReferenceGeneral.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot1: DataSnapshot) {
                val keysGeneral = dataSnapshot1.children.map { it.key }
                databaseReferenceComunity.addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot2: DataSnapshot) {
                        val keysComunity = dataSnapshot2.children.map { it.key }
                        val unionKeys = keysGeneral.union(keysComunity)
                        vibrationIdList.addAll(unionKeys.filterNotNull())
                        val adapter = ArrayAdapter(
                            this@LearnActivity,
                            android.R.layout.simple_dropdown_item_1line,
                            vibrationIdList
                        )
                        inputText.setAdapter(adapter)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Lidar com erros de leitura do banco de dados, se necessário
                    }
                })
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Lidar com erros de leitura do banco de dados, se necessário
            }
        })
    }
}

