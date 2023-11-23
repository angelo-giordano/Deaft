package com.example.deaft
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
class MenuActivity : AppCompatActivity() {

    private lateinit var btnTranslate: Button
    private lateinit var btnPractice: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        btnTranslate = findViewById(R.id.btnTranslate)
        btnPractice = findViewById(R.id.btnPractice)


        btnTranslate.setOnClickListener {
            val intent = Intent(this, TranslateActivity::class.java)
            startActivity(intent)
        }

        btnPractice.setOnClickListener {
            val intent = Intent(this, PracticeActivity::class.java)
            startActivity(intent)
        }
    }
}
