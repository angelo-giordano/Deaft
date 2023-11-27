package com.example.deaft


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RecognizeActivity : AppCompatActivity() {

    private lateinit var editTextUserTranscription: EditText
    private lateinit var btnListenAgain: Button
    private lateinit var btnCheckTranscription: Button
    private lateinit var btnNextWord: Button
    private var currentWord: String = ""
    private lateinit var textViewWordToTranscribe: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recognize)

        editTextUserTranscription = findViewById(R.id.editTextUserTranscription)
        btnListenAgain = findViewById(R.id.btnListenAgain)
        btnCheckTranscription = findViewById(R.id.btnCheckTranscription)
        btnNextWord = findViewById(R.id.btnNextWord)
        textViewWordToTranscribe = findViewById(R.id.viewAnswer)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        VibrationUtil.showEnableHapticFeedbackMessage(this)
        getNextWord()

        btnListenAgain.setOnClickListener {
            VibrationUtil.showEnableHapticFeedbackMessage(this)
            GlobalScope.launch(Dispatchers.Main) {
                performVibration(currentWord)
            }
        }

        btnCheckTranscription.setOnClickListener {
            checkAnswer()
        }

        btnNextWord.setOnClickListener {
            getNextWordWithAnimation()
        }

    }

    private fun getNextWord() {
        val databaseReferenceGeneral = FirebaseDatabase.getInstance().getReference("vibracoes")
        val databaseReferenceComunity = FirebaseDatabase.getInstance().getReference("vibracoes-comunidade")
        databaseReferenceGeneral.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot1: DataSnapshot) {
                val keysGeneral = dataSnapshot1.children.map { it.key }
                databaseReferenceComunity.addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot2: DataSnapshot) {
                        val keysComunity = dataSnapshot2.children.mapNotNull { it.key }.filter { it != "0" }
                        val unionKeys = keysGeneral.union(keysComunity)
                        GlobalScope.launch(Dispatchers.Main) {
                            currentWord = unionKeys.randomOrNull() ?: "Foi nulo"

                            if (!currentWord.isNullOrBlank()) {
                                performVibration(currentWord)
                                editTextUserTranscription.text.clear()
                            }
                        }

                }
                    override fun onCancelled(databaseError: DatabaseError) {
                        // Lidar com erros de leitura do banco de dados, se necessário
                        // Note que você pode querer tratar erros aqui, dependendo dos requisitos do seu aplicativo
                    }
                })
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Lidar com erros de leitura do banco de dados, se necessário
                // Note que você pode querer tratar erros aqui, dependendo dos requisitos do seu aplicativo
            }
        })
    }


    private fun getNextWordWithAnimation() {
        val intent = Intent(this, RecognizeActivity::class.java)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this)
        startActivity(intent, options.toBundle())
        finish()
    }

    private fun checkAnswer() {
        val userTranscription = editTextUserTranscription.text.toString().lowercase().trim()

        if (userTranscription == currentWord) {
            textViewWordToTranscribe.setTextColor(ContextCompat.getColor(this, R.color.green))
            textViewWordToTranscribe.text = "Você acertou!"
        } else {
            textViewWordToTranscribe.setTextColor(ContextCompat.getColor(this, R.color.red))
            textViewWordToTranscribe.text = "Errou! A palavra era " + currentWord
        }
    }

    private suspend fun performVibration(text: String) {
        withContext(Dispatchers.Default) {
            if (!Python.isStarted()) {
                Python.start(AndroidPlatform(this@RecognizeActivity))
            }

            val processedText = VibrationUtil.translateToVib(text)
            processedText.forEach { element ->
                VibrationUtil.vibrate(this@RecognizeActivity, element)
                delay(700)
            }
        }
    }
}

