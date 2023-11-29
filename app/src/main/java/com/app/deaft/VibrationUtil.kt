package com.app.deaft

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.provider.Settings
import android.widget.Toast
import com.chaquo.python.Python
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class VibrationUtil {
    companion object {

        suspend fun translateToVib(text: String): List<Long> = withContext(Dispatchers.IO) {
            Python.getInstance().getModule("main").callAttr("set_vib", text).asList()
                .map { it.toLong() }
        }

        fun vibrate(context: Context, milliseconds: Long) {
            val vibrator: Vibrator? = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?

            vibrator?.let {
                if (it.hasVibrator()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        // Para Android Oreo e versões mais recentes
                        it.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE))
                    } else {
                        // Para versões anteriores ao Android Oreo
                        @Suppress("DEPRECATION")
                        it.vibrate(milliseconds)
                    }
                }
                }
            }
        fun isHapticFeedbackEnabled(context: Context): Boolean {
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

            // Verificar se a vibração está ativada nas configurações do sistema
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.hasVibrator() && Settings.System.getInt(
                    context.contentResolver,
                    Settings.System.VIBRATE_ON,
                    0
                ) == 1
            } else {
                vibrator.hasVibrator()
            }
        }

        fun showEnableHapticFeedbackMessage(context: Context) {
            if (!isHapticFeedbackEnabled(context)) {
                val toast = Toast.makeText(context, "Por favor, ative o feedback tátil nas configurações do sistema.", Toast.LENGTH_LONG)
                toast.show()
            }
        }
        suspend fun translateToVibDict(text: String): List<Long> = withContext(Dispatchers.IO) {
            Python.getInstance().getModule("main").callAttr("set_vib_dict", text).asList()
                .map { it.toLong() }
        }
    }

}