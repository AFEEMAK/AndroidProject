package com.afee.recuritingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {

    private lateinit var btnlogout: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnlogout = findViewById(R.id.btnlogout)
        btnlogout.setOnClickListener{
            Firebase.auth.signOut()

            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)

            Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show()

        }

        val viewCandidatesButton: Button = findViewById(R.id.viewCandidatesButton)
        viewCandidatesButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, CandidateActivity::class.java))
            finish()
        }
    }
}