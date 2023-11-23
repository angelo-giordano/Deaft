package com.example.deaft
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform

class PracticeActivity : AppCompatActivity() {

    private lateinit var btnSim: Button
    private lateinit var btnNao: Button
    private lateinit var btnTalvez: Button
    private lateinit var btnTudo: Button
    private lateinit var btnCerto: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice)

        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }

        btnNao = findViewById(R.id.btnNao)
        btnSim = findViewById(R.id.btnSim)
        btnTalvez = findViewById(R.id.btnTalvez)
        btnTudo = findViewById(R.id.btnTudo)
        btnCerto = findViewById(R.id.btnCerto)

        btnNao.setOnClickListener {
            var processedText = translateToVib("não")
            processedText.forEach { element ->
                vibrate(element)
            }
        }
        btnSim.setOnClickListener {
            var processedText = translateToVib("sim")
            processedText.forEach { element ->
                vibrate(element)
            }
        }
        btnTudo.setOnClickListener {
            var processedText = translateToVib("tudo")
            processedText.forEach { element ->
                vibrate(element)
            }
        }
        btnTalvez.setOnClickListener {
            var processedText = translateToVib("talvez")
            processedText.forEach { element ->
                vibrate(element)
            }
        }
        btnCerto.setOnClickListener {
            var processedText = translateToVib("certo")
            processedText.forEach { element ->
                vibrate(element)
            }
        }
    }

    private fun translateToVib(text: String): List<Long> {
        // Chama a função Python diretamente usando Chaquopy

        return Python.getInstance().getModule("main").callAttr("set_vib", text).asList().map{ it.toLong() }
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
}
