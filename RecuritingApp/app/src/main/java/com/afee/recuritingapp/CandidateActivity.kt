package com.afee.recuritingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class CandidateActivity : AppCompatActivity() {
    private var adapter: CandidateAdapter? = null
    private lateinit var btnlogout: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate)

        btnlogout = findViewById(R.id.btnlogout)
        btnlogout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)

            Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show()
            finish()
        }

        val query = FirebaseDatabase.getInstance().reference.child("Candidates")
        val options =
            FirebaseRecyclerOptions.Builder<Candidates>().setQuery(query, Candidates::class.java)
                .build()
        adapter = CandidateAdapter(options)


        val rView: RecyclerView = findViewById(R.id.candidatesRView)
        rView.layoutManager = LinearLayoutManager(this)
        rView.adapter = adapter


        val viewPostsButton: Button = findViewById(R.id.viewPostsButton)
        viewPostsButton.setOnClickListener {

            startActivity(Intent(this@CandidateActivity, MainActivity::class.java))

            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }
    }

