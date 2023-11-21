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
import android.view.View

class MainActivity : AppCompatActivity() {

    private lateinit var btnNavigate: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnNavigate = findViewById(R.id.btnNavigate)
        btnNavigate.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
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


