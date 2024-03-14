package com.afee.recuritingapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.FirebaseDatabase

class CandidateActivity : AppCompatActivity() {
    private var adapter: CandidateAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_candidate)

        val query = FirebaseDatabase.getInstance().reference.child("Candidates")
        val options = FirebaseRecyclerOptions.Builder<Candidates>().setQuery(query, Candidates::class.java).build()
        adapter = CandidateAdapter(options)


        val rView : RecyclerView = findViewById(R.id.candidatesRView)
        rView.layoutManager = LinearLayoutManager(this)
        rView.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        adapter?.startListening()
    }
    }

