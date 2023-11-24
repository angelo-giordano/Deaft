package com.example.deaft

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform

class LanguageSelectionFragment : Fragment() {

    private lateinit var spinnerLanguages: Spinner
    private lateinit var btnSelectLanguage: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_language_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerLanguages = view.findViewById(R.id.spinner_languages)
        btnSelectLanguage = view.findViewById(R.id.btn_select_language)

        // Lógica específica do RecognizeFragment
        setupLanguageSelectionFragment()
    }

    private fun setupLanguageSelectionFragment() {
        // Implemente a lógica específica do RecognizeFragment aqui
        //textViewRecognize.text = "Esta é a tela Recognize!"
        // ...
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(requireContext()))
        }

        val languages = listOf("Português", "Inglês", "Espanhol", "Francês", "Alemão", "Mandarim", "Japonês", "Coreano")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLanguages.adapter = adapter

        btnSelectLanguage.setOnClickListener {
            val selectedLanguage = spinnerLanguages.selectedItem.toString()
            // Aqui você pode enviar a língua selecionada de volta para a atividade principal ou
            // realizar qualquer outra ação necessária
            val lang = defineLanguage(selectedLanguage)

            val intent = Intent(requireContext(), TranslateActivity::class.java)
            intent.putExtra("lang", lang)
            startActivity(intent)
        }

    }
    private fun defineLanguage(language: String): String {
        // Chama a função Python diretamente usando Chaquopy

        return Python.getInstance().getModule("main").callAttr("define_language", language).toString()
    }

}

