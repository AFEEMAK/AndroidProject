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
import com.google.firebase.database.*
import com.google.firebase.auth.FirebaseUser


class MainActivity : AppCompatActivity() {
    private var adapter: PostAdapter? = null
        private lateinit var btnlogout: Button
    private var currentUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currentUser = FirebaseAuth.getInstance().currentUser
        btnlogout = findViewById(R.id.btnlogout)
        btnlogout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)

            Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show()
            finish()
        }

        val viewCandidatesButton: Button = findViewById(R.id.viewCandidatesButton)
        viewCandidatesButton.setOnClickListener {
            startActivity(Intent(this@MainActivity, CandidateActivity::class.java))
            finish()
        }


        val connectionsRef = FirebaseDatabase.getInstance().reference.child("Connections")
        connectionsRef.orderByChild("user_id").equalTo(currentUser?.uid)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    val connectedUserIds = mutableListOf<String>()
                        for (data in snapshot.children) {
                         val connectedUserId = data.child("connected_user_id").getValue(String::class.java)
                        connectedUserId?.let { connectedUserIds.add(it) }
                    }
                    if (connectedUserIds.isNotEmpty()) {
                        displayConnectedPost(connectedUserIds)
                    } else {
                        Toast.makeText(this@MainActivity, "No Posts To sHOW", Toast.LENGTH_SHORT).show()
                        }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MainActivity, "database error cant connect", Toast.LENGTH_SHORT).show()
                }
            })

                    }
    private fun displayConnectedPost(connectedUserIds: List<String>) {
        val postReference = FirebaseDatabase.getInstance().reference.child("Posts")
        val query = postReference.orderByChild("user_id")
            .startAt(connectedUserIds[0])
            .endAt(connectedUserIds[connectedUserIds.size - 1])
            val options = FirebaseRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()
                 adapter = PostAdapter(options)

        val rView: RecyclerView = findViewById(R.id.postsRView)
        rView.layoutManager = LinearLayoutManager(this)
        rView.adapter = adapter

        adapter?.startListening()
    }

}
