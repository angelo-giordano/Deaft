package com.example.deaft
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var btnLogin: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin = findViewById(R.id.btn)


        btnLogin.setOnClickListener {
            val intent = Intent(this, TranslateActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}
