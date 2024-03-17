package com.afee.recuritingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class DetailActivity : AppCompatActivity() {
    private lateinit var candidateId: String
    private lateinit var connectButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        candidateId = intent.getStringExtra("candidateId") ?: ""
        connectButton = findViewById(R.id.connect_button)

        checkIfConnected()

        connectButton.setOnClickListener {
            if (connectButton.text == "Connected") {
                removeConnection()
            } else {
                addConnection()
            }
        }
    }

    private fun checkIfConnected() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val connectionRef = FirebaseDatabase.getInstance().reference.child("Connections")
        connectionRef.orderByChild("user_id").equalTo(currentUserUid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var connectionMade = false
                    for (data in snapshot.children) {
                        val connection = data.getValue(Connection::class.java)
                        if (connection?.connected_user_id == candidateId) {
                            connectionMade = true
                            break
                        }
                    }
                    if (connectionMade) {
                        connectButton.text = "Connected"
                    } else {
                        connectButton.text = "Connect"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@DetailActivity, "Database error", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun addConnection() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val connectionRef = FirebaseDatabase.getInstance().reference.child("Connections")
        val connectionKey = connectionRef.push().key ?: ""
        connectionRef.child(connectionKey).setValue(Connection(currentUserUid, candidateId))
            .addOnSuccessListener {
                Toast.makeText(this@DetailActivity, "Connected to candidate", Toast.LENGTH_SHORT).show()
                connectButton.text = "Connected"
            }.addOnFailureListener {
                Toast.makeText(this@DetailActivity, "Failed to connect", Toast.LENGTH_SHORT).show()
            }
    }

    private fun removeConnection() {
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val connectionRef = FirebaseDatabase.getInstance().reference.child("Connections")
        connectionRef.orderByChild("user_id").equalTo(currentUserUid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (data in snapshot.children) {
                        val connection = data.getValue(Connection::class.java)
                         if (connection?.connected_user_id == candidateId) {
                            data.ref.removeValue().addOnSuccessListener {
                                  Toast.makeText(this@DetailActivity, "Candidate removed", Toast.LENGTH_SHORT).show()
                                connectButton.text = "Connect"
                            }.addOnFailureListener {
                                Toast.makeText(this@DetailActivity, "Failed to remove candidate", Toast.LENGTH_SHORT).show()
                            }
                            break
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@DetailActivity, "Database error", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
