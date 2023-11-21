package com.example.deaft

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Vibrator
import android.os.VibratorManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.chaquo.python.Python
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.widget.Toast
import java.util.Locale
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

class MainActivity : AppCompatActivity() {

    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var transcribedText: TextView
    private lateinit var startButton: Button
    private lateinit var handler: Handler

    companion object {
        private const val RECORD_AUDIO_PERMISSION_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
            speechRecognizer.setRecognitionListener(object : RecognitionListener {
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

        val thread = Thread {
            while (true) {
                runOnUiThread {
                    speechRecognizer.startListening(intent)
                }

                // Aguarde um curto período de tempo antes de começar a ouvir novamente
                try {
                    Thread.sleep(500) // Ajuste conforme necessário
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }
        thread.start()
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
        speechRecognizer.destroy()
    }

    private fun vibrate(milliseconds: Long) {
        val vibrator: Vibrator? = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?

        if (vibrator != null && vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // Para Android Oreo e versões mais recentes
                vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE))
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




/*class MainActivity : ComponentActivity() {

    fun vibratePhone() {
        val vibrator = if (Build.VERSION.SDK_INT >= 31) {
            val vibratorManager =
                applicationContext.getSystemService(VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            applicationContext.getSystemService(VIBRATOR_SERVICE) as Vibrator
        }
    }
}

override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val btn = findViewById<Button>(R.id.btn)
    val tv = findViewById<TextView>(R.id.text_view)

    if (! Python.isStarted()) {
        Python.start(AndroidPlatform(this))
    }

    val py = Python.getInstance()

    btn.setOnClickListener(View.OnClickListener {
        view ->
        val pyObj = py.getModule("main").callAttr("test")
        tv.setText(pyObj.toString())
    })

}
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
Text(
    text = "Hello $name!",
    modifier = modifier
)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
DeaftTheme {
    Greeting("Android")
}
}
*/


