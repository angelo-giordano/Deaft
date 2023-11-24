package com.example.deaft

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform

class PracticeFragment : Fragment() {

    private lateinit var btnSim: Button
    private lateinit var btnNao: Button
    private lateinit var btnTalvez: Button
    private lateinit var btnTudo: Button
    private lateinit var btnCerto: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar o layout para este fragmento
        return inflater.inflate(R.layout.fragment_practice, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnNao = view.findViewById(R.id.btnNao)
        btnSim = view.findViewById(R.id.btnSim)
        btnTalvez = view.findViewById(R.id.btnTalvez)
        btnTudo = view.findViewById(R.id.btnTudo)
        btnCerto = view.findViewById(R.id.btnCerto)
        setupPracticeFragment()
    }

    private fun setupPracticeFragment() {

        btnNao.setOnClickListener {
            performAction("não")
        }
        btnSim.setOnClickListener {
            performAction("sim")
        }
        btnTudo.setOnClickListener {
            performAction("tudo")
        }
        btnTalvez.setOnClickListener {
            performAction("talvez")
        }
        btnCerto.setOnClickListener {
            performAction("certo")
        }
    }

    private fun performAction(inputText: String) {
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(requireContext()))
        }

        val processedText = VibrationUtil.translateToVib(inputText)
        processedText.forEach { element ->
            VibrationUtil.vibrate(requireContext(), element)
            Thread.sleep(500)
        }
    }
}
