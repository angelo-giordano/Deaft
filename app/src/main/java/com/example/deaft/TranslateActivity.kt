package com.example.deaft

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import com.chaquo.python.Python
import android.content.Intent
import android.speech.RecognizerIntent
import java.util.*
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.os.VibrationEffect
import android.provider.Settings
import android.widget.Button
import android.widget.TextView
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.chaquo.python.android.AndroidPlatform

class TranslateActivity : AppCompatActivity() {

    private lateinit var transcribedText: TextView
    private lateinit var startButton: Button
    private lateinit var handler: Handler
    private var speechRecognizer: SpeechRecognizer? = null
    private var recognitionThread: Thread? = null
    companion object {
        private const val RECORD_AUDIO_PERMISSION_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translate)

        transcribedText = findViewById(R.id.text_view)
        startButton = findViewById(R.id.btn)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_PERMISSION_CODE
            )
        } else {
            initializeSpeechRecognizer()
        }

        handler = Handler(Looper.getMainLooper()) {
            val result = it.obj as String
            updateUI(result)
            true
        }

        startButton.setOnClickListener {
            startSpeechRecognition()
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
                        val processedText = translateToVib(matches[0])
                        processedText.forEach { element ->
                            vibrate(element)
                        }
                        sendMessageToUI(processedText.toString())
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

    private fun startSpeechRecognition() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR") // Altere para o idioma desejado

        recognitionThread = Thread {
            while (!Thread.interrupted()) {
                runOnUiThread {
                    if (!isFinishing) {  // Verifique se a atividade ainda não está sendo finalizada
                        speechRecognizer?.startListening(intent)
                    }
                }

                // Aguarde um curto período de tempo antes de começar a ouvir novamente
                try {
                    Thread.sleep(500) // Ajuste conforme necessário
                } catch (e: InterruptedException) {
                    Thread.currentThread().interrupt()
                }
            }
        }
        recognitionThread?.start()
    }


    private fun translateToVib(text: String): List<Long> {
        // Chama a função Python diretamente usando Chaquopy

        return Python.getInstance().getModule("main").callAttr("set_vib", text).asList().map{ it.toLong() }
    }

    private fun sendMessageToUI(processedText: String) {
        val message = Message.obtain()
        message.obj = processedText
        handler.sendMessage(message)
    }

    private fun updateUI(processedText: String) {
        transcribedText.text = processedText
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

    private fun vibrate(milliseconds: Long) {
        val vibrator: Vibrator? = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?

        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Para Android Oreo e versões mais recentes
                vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE))
                Thread.sleep(500)
            } else {
                // Para versões anteriores ao Android Oreo
                @Suppress("DEPRECATION")
                vibrator.vibrate(milliseconds)
            }
        }
    }

    private fun isVibrationEnabled(context: Context): Boolean {
        val mode = Settings.System.getInt(context.contentResolver, Settings.System.VIBRATE_WHEN_RINGING, 0)
        return mode != 0
    }
}
