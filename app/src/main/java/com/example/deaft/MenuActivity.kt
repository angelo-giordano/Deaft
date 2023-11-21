package com.example.deaft
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
class MenuActivity : AppCompatActivity() {

    private lateinit var btnNavigate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        btnNavigate = findViewById(R.id.btnNavigate)
        btnNavigate.setOnClickListener {
            val intent = Intent(this, TranslateActivity::class.java)
            startActivity(intent)
        }
    }
}
