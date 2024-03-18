package com.afee.recuritingapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class DetailActivity : AppCompatActivity() {
    private lateinit var candidateId: String
    private lateinit var connectButton: Button
    private lateinit var btnlogout : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val viewCandidatesButton: Button = findViewById(R.id.viewCandidatesButton)
        viewCandidatesButton.setOnClickListener {
            startActivity(Intent(this@DetailActivity, CandidateActivity::class.java))
            finish()
        }
        btnlogout = findViewById(R.id.btnlogout)
        btnlogout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)

            Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show()
            finish()
        }

        candidateId = intent.getStringExtra("candidateId") ?: ""
        connectButton = findViewById(R.id.connect_button)
        candidateId = intent.getStringExtra("candidateId") ?:""
        Log.i("Id",candidateId)
        val userName : TextView = findViewById(R.id.user_name)
        val userTitle : TextView = findViewById(R.id.user_title)
        val userCompany : TextView = findViewById(R.id.user_company)
        val userDesc : TextView = findViewById(R.id.user_desc)
        val userPhone : TextView = findViewById(R.id.user_phone)
        val userEmail : TextView = findViewById(R.id.user_email)
        val userPhoto : ImageView = findViewById(R.id.user_photo)


        val candidate = FirebaseDatabase.getInstance().reference.child("Candidates").child(candidateId)
        candidate.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val candidate = snapshot.getValue(Candidates::class.java)
                    if (candidate != null) {
                        userName.text = candidate.name
                        userTitle.text = candidate.position
                        userCompany.text = candidate.company
                        userDesc.text = candidate.desc
                        userPhone.text = candidate.phone
                        userEmail.text = candidate.email

                        val storRef: StorageReference = FirebaseStorage.getInstance().getReferenceFromUrl(candidate.photo)
                        Glide.with(userPhoto.context).load(storRef).into(userPhoto)

                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.w("Error", "Failed to read value.", error.toException())
            }
        })




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