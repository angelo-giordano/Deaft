package com.example.deaft


import android.content.Intent
import android.os.Bundle
import android.transition.Explode
import android.view.Window
import com.chaquo.python.Python
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.content.ContextCompat
import com.chaquo.python.android.AndroidPlatform
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
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
    private lateinit var btnExit: Button
    private lateinit var btnNextWord: Button
    private lateinit var databaseReference: DatabaseReference
    private var currentWord: String = ""
    private lateinit var textViewWordToTranscribe: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recognize)

        editTextUserTranscription = findViewById(R.id.editTextUserTranscription)
        btnListenAgain = findViewById(R.id.btnListenAgain)
        btnCheckTranscription = findViewById(R.id.btnCheckTranscription)
        btnExit = findViewById(R.id.btnExit)
        btnNextWord = findViewById(R.id.btnNextWord)
        databaseReference = FirebaseDatabase.getInstance().getReference("vibracoes")
        textViewWordToTranscribe = findViewById(R.id.viewAnswer)


        getNextWord()

        btnListenAgain.setOnClickListener {
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

        btnExit.setOnClickListener {
            finish()
        }

    }

    private fun getNextWord() {
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                GlobalScope.launch(Dispatchers.Main) {
                    val wordSnapshot = dataSnapshot.children.mapNotNull { it.key }
                    val words = wordSnapshot.random()

                    if (!words.isNullOrBlank()) {
                        currentWord = words
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


    private fun getNextWordWithAnimation() {
        val intent = Intent(this, RecognizeActivity::class.java)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this)
        finish()
        startActivity(intent, options.toBundle())
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
                delay(500)
            }
        }
    }
}

