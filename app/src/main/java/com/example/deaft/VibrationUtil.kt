package com.example.deaft

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.VibrationEffect
import android.os.Vibrator
import com.chaquo.python.PyObject
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform
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
        suspend fun translateToVibDict(text: String): List<Long> = withContext(Dispatchers.IO) {
            Python.getInstance().getModule("main").callAttr("set_vib_dict", text).asList()
                .map { it.toLong() }
        }
    }

}