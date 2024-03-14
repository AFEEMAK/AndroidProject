package com.afee.recuritingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val viewCandidatesButton: Button = findViewById(R.id.viewCandidatesButton)
        viewCandidatesButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, CandidateActivity::class.java))
            finish()
        }
    }
}