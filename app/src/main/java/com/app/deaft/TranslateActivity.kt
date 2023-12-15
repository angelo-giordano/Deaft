package com.app.deaft

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TranslateActivity : AppCompatActivity() {

    private lateinit var handler: Handler
    private var speechRecognizer: SpeechRecognizer? = null
    private var recognitionThread: Thread? = null
    companion object {
        private const val RECORD_AUDIO_PERMISSION_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translate)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_PERMISSION_CODE
            )
        } else {
            VibrationUtil.showEnableHapticFeedbackMessage(this)
            initializeSpeechRecognizer()
        }

        val selectedLanguage = intent.getStringExtra("lang")
        val gifImageView: ImageView = findViewById(R.id.gifImageView)

        Glide.with(this)
            .asGif()
            .load("https://assets-v2.lottiefiles.com/a/78a37ef6-1186-11ee-bb94-675733c8ad9a/fefZso2h91.gif")
            .into(gifImageView)
        startSpeechRecognition(selectedLanguage?:"")

        handler = Handler(Looper.getMainLooper()) {
            true
        }
    }

    private fun initializeSpeechRecognizer() {
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }


        if (SpeechRecognizer.isRecognitionAvailable(this)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
            speechRecognizer?.setRecognitionListener(object : RecognitionListener {
                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    if (!matches.isNullOrEmpty()) {
                        GlobalScope.launch {
                            val processedText = VibrationUtil.translateToVib(matches[0])
                            processedText.forEach { element ->
                                VibrationUtil.vibrate(this@TranslateActivity, element)
                                delay(800)
                            }
                        }
                    }
                }

                override fun onReadyForSpeech(params: Bundle?) {}
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}

                override fun onError(error: Int) {
                    // Handle recognition error if needed
                }

                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
        }
    }

    private fun startSpeechRecognition(selectedLanguage: String) {

        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, selectedLanguage) // Altere para o idioma desejado

        recognitionThread = Thread {
            while (!Thread.interrupted()) {
                runOnUiThread {
                    if (!isFinishing) {  // Verifique se a atividade ainda não está sendo finalizada
                        VibrationUtil.showEnableHapticFeedbackMessage(this)
                        speechRecognizer?.startListening(intent)
                    }
                }

                // Aguarde um curto período de tempo antes de começar a ouvir novamente
                try {
                    Thread.sleep(1000) // Ajuste conforme necessário
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                }
            }
        }
        recognitionThread?.start()
    }


    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer?.destroy()
    }

    override fun onPause() {
        super.onPause()

        stopSpeechRecognition()
    }
    private fun stopSpeechRecognition() {
        recognitionThread?.interrupt()  // Pare o thread de reconhecimento
        speechRecognizer?.stopListening()
        speechRecognizer?.cancel()
    }

}
