package com.example.deaft

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform

class LanguageSelectionActivity : AppCompatActivity() {

    private lateinit var spinnerLanguages: Spinner
    private lateinit var btnSelectLanguage: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language_selection)

        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this))
        }

        spinnerLanguages = findViewById(R.id.spinner_languages)
        btnSelectLanguage = findViewById(R.id.btn_select_language)

        // Adicione as línguas desejadas ao spinner
        val languages = listOf("Português", "Inglês", "Espanhol", "Francês", "Alemão", "Mandarim", "Japonês", "Coreano")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languages)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerLanguages.adapter = adapter

        btnSelectLanguage.setOnClickListener {
            val selectedLanguage = spinnerLanguages.selectedItem.toString()
            // Aqui você pode enviar a língua selecionada de volta para a atividade principal ou
            // realizar qualquer outra ação necessária
            val lang = defineLanguage(selectedLanguage)

            val intent = Intent(this, TranslateActivity::class.java)
            intent.putExtra("lang", lang)
            startActivity(intent)
        }
    }

    private fun defineLanguage(language: String): String {
        // Chama a função Python diretamente usando Chaquopy

        return Python.getInstance().getModule("main").callAttr("define_language", language).toString()
    }
}
