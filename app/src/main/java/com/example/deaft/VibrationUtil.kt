package com.example.deaft

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import com.chaquo.python.Python

class VibrationUtil {
    companion object {
         fun translateToVib(text: String): List<Long> {
            // Chama a função Python diretamente usando Chaquopy

            return Python.getInstance().getModule("main").callAttr("set_vib", text).asList().map{ it.toLong() }
        }

        fun vibrate(context: Context, milliseconds: Long) {
            val vibrator: Vibrator? = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?

            if (vibrator != null && vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    // Para Android Oreo e versões mais recentes
                    vibrator.vibrate(VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE))

                    // Usando um Handler para adicionar um atraso
                } else {
                    // Para versões anteriores ao Android Oreo
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(milliseconds)
                }
            }
        }
    }
}