package com.afee.recuritingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        val introButton: Button = findViewById(R.id.intro_button)
        introButton.setOnClickListener {

            startActivity(Intent(this@IntroActivity, MainActivity::class.java))

            finish()
        }
    }
}
